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
            isMinifyEnabled = false // 必须关闭混淆，否则入口类会被改得面目全非
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
    // 使用 JitPack 上 API 100 对应的确切版本号
    compileOnly("io.github.libxposed:api:-100-ge2588ee-22") 
}
