package com.example.module;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.KeyEvent;
import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;
import java.lang.reflect.Method;

public class MainModule extends XposedModule {
    private static final long DOUBLE_TAP_TIMEOUT = 450;
    private long lastVolumeDownTime = 0;
    private long lastVolumeUpTime = 0;

    @Override
    public void onSystemServerStarting() {
        try {
            Class<?> pwmClass = Class.forName("com.android.server.policy.PhoneWindowManager");
            Method interceptMethod = null;
            for (Method m : pwmClass.getDeclaredMethods()) {
                if (m.getName().equals("interceptKeyBeforeQueueing") && m.getParameterTypes().length >= 1) {
                    if (m.getParameterTypes()[0] == KeyEvent.class) {
                        interceptMethod = m;
                        break;
                    }
                }
            }
            if (interceptMethod == null) return;

            hook(interceptMethod).intercept(new XposedInterface.Hooker() {
                @Override
                public Object intercept(XposedInterface.Chain chain) throws Throwable {
                    KeyEvent event = null;
                    for (Object arg : chain.getArgs()) {
                        if (arg instanceof KeyEvent) {
                            event = (KeyEvent) arg;
                            break;
                        }
                    }
                    if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                        int keyCode = event.getKeyCode();
                        long now = SystemClock.uptimeMillis();
                        // 音量下键双击
                        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                            if (now - lastVolumeDownTime < DOUBLE_TAP_TIMEOUT) {
                                lastVolumeDownTime = 0;
                                triggerRecording(chain);
                                return null; // 拦截事件，防止音量变化
                            }
                            lastVolumeDownTime = now;
                        } 
                        // 音量上键双击
                        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                            if (now - lastVolumeUpTime < DOUBLE_TAP_TIMEOUT) {
                                lastVolumeUpTime = 0;
                                triggerRecording(chain);
                                return null;
                            }
                            lastVolumeUpTime = now;
                        }
                    }
                    return chain.proceed();
                }
            });
        } catch (Throwable t) {
            log("Hook failed: " + t);
        }
    }

    private void triggerRecording(XposedInterface.Chain chain) {
        try {
            Object pwmInstance = chain.getThisObject();
            Context sysContext = (Context) io.github.libxposed.api.XposedInterface.Helper.getObjectField(pwmInstance, "mContext");
            if (sysContext != null) {
                Intent intent = new Intent();
                intent.setAction("com.flambo.recorder.ACTION_TOGGLE");
                intent.setPackage("com.flambo.recorder");
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                sysContext.sendBroadcast(intent);
            }
        } catch (Throwable t) {
            log("Trigger failed: " + t);
        }
    }
}
