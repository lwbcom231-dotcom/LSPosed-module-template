package com.example.module;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.KeyEvent;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainModule implements IXposedHookLoadPackage {
    private static final long DOUBLE_TAP_TIMEOUT = 450;
    private long lastVolumeDownTime = 0;
    private long lastVolumeUpTime = 0;

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        // 传统 API 必须过滤包名，只拦截系统框架
        if (!"android".equals(lpparam.packageName)) return;

        try {
            Class<?> pwmClass = XposedHelpers.findClass("com.android.server.policy.PhoneWindowManager", lpparam.classLoader);
            
            XposedHelpers.findAndHookMethod(pwmClass, "interceptKeyBeforeQueueing", KeyEvent.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    KeyEvent event = (KeyEvent) param.args[0];
                    if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                        int keyCode = event.getKeyCode();
                        long now = SystemClock.uptimeMillis();

                        // 音量下键双击
                        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                            if (now - lastVolumeDownTime < DOUBLE_TAP_TIMEOUT) {
                                lastVolumeDownTime = 0;
                                triggerRecording(param.thisObject);
                                param.setResult(null); // 拦截按键
                            } else {
                                lastVolumeDownTime = now;
                            }
                        } 
                        // 音量上键双击
                        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                            if (now - lastVolumeUpTime < DOUBLE_TAP_TIMEOUT) {
                                lastVolumeUpTime = 0;
                                triggerRecording(param.thisObject);
                                param.setResult(null); // 拦截按键
                            } else {
                                lastVolumeUpTime = now;
                            }
                        }
                    }
                }
            });
        } catch (Throwable t) {
            XposedBridge.log("Hook failed: " + t);
        }
    }

    private void triggerRecording(Object pwmInstance) {
        try {
            Context sysContext = (Context) XposedHelpers.getObjectField(pwmInstance, "mContext");
            if (sysContext != null) {
                Intent intent = new Intent();
                intent.setAction("com.flambo.recorder.ACTION_TOGGLE");
                intent.setPackage("com.flambo.recorder");
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                sysContext.sendBroadcast(intent);
            }
        } catch (Throwable t) {
            XposedBridge.log("Trigger failed: " + t);
        }
    }
}
