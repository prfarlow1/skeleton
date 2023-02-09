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
    versionCatalogs {
        create("libs") {
            library("androidx-core", "androidx.core:core-ktx:1.9.0")
            library("androidx-appcompat", "androidx.appcompat:appcompat:1.7.0-alpha02")
        }
    }
}
rootProject.name = "Skeleton"
include(":app")
