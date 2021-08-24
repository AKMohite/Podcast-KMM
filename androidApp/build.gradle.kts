plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = PodConfig.compileSDKVersion
    defaultConfig {
        applicationId = PodConfig.applicationId
        minSdk = PodConfig.minSdkVersion
        targetSdk = PodConfig.targetSdkVersion
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