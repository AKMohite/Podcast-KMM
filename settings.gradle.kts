pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Pocket_Notes"
include(":androidApp")
include(":shared")
include(":benchmark")
include(":baselineprofiles")
include(":wearos")
include(":core:designsystem")
include(":core:remote")
include(":core:common")
include(":core:database")
include(":core:feature:data")
