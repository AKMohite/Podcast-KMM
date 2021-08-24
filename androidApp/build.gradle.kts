plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(PodConfig.compileSDK)
    defaultConfig {
        applicationId = PodConfig.applicationId
        minSdkVersion(PodConfig.minSdk)
        targetSdkVersion(PodConfig.targetSdk)
        versionCode = PodConfig.versionCode
        versionName = PodConfig.versionName
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(project(PodModule.shared))
    implementation(PodLib.material)
    implementation(PodLib.appCompat)
    implementation(PodLib.constraintLayout)
}