pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "MediaBoxBasePlugin"
//include(":TestPlugin")
val pluginApi = ":MediaBoxPluginApi"
include(pluginApi)
project(pluginApi).projectDir = File("./submodules/MediaBoxPlugin/pluginApi")
include(":BaseVideoPluginFramework")
