plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.module"
    compileSdk = 34 // 改回 34，云端环境绝对支持

    defaultConfig {
        applicationId = "com.example.module"
        minSdk = 31
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // ⚠️ 必须关掉！防止入口类被混淆
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // 退回 17，最稳
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        checkReleaseBuilds = false
        targetSdk = 34
    }

    dependenciesInfo {
        includeInApk = false
    }
}

dependencies {
    compileOnly("androidx.annotation:annotation:1.9.1")
    compileOnly(project(":libxposed-api")) 
}
