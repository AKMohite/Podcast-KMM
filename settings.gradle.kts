pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    // latest version of r8
    buildscript {
        repositories {
            maven {
                url = uri("https://storage.googleapis.com/r8-releases/raw/main")
            }
            dependencies {
                classpath("com.android.tools:r8:f4c9082149ea97a824cc29130e51df1cb32a1df1")
            }
        }
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
include(":pocketnotes-wearos")
include(":benchmark")
include(":baselineprofiles")
