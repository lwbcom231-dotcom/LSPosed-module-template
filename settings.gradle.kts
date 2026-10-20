pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 从 JitPack 拉取 libxposed 依赖
        maven { url = uri("https://jitpack.io") } 
    }
}

rootProject.name = "LSPosed-module-template"
include(":app")
