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
        // 官方核心仓库，这次绝对能找到依赖
        maven { url = uri("https://raw.githubusercontent.com/libxposed/maven/main") }
    }
}

rootProject.name = "LSPosed-module-template"
include(":app")
// 注意：这里故意不 include 那个破本地的 libxposed-api 文件夹了
