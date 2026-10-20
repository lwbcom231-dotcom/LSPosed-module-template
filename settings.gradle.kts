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
        // 👇 关键：手动添加 Xposed 官方归档仓库
        maven { url = uri("https://api.xposed.info/") }
    }
}

rootProject.name = "LSPosed-module-template"
include(":app")
