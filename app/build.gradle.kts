plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.module"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.module"
        minSdk = 31
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // 绝对不能开混淆
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        checkReleaseBuilds = false
        targetSdk = 34
    }
}

dependencies {
    compileOnly("androidx.annotation:annotation:1.9.1")
    // 直接从云端拉取 API 100 版本，不用本地文件
    compileOnly("io.github.libxposed:api:100")
}
