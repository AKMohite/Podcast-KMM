plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(PodModule.shared))
    implementation(PodLib.material)
    implementation(PodLib.appCompat)
    implementation(PodLib.constraintLayout)
    implementation(PodLib.hiltAndroid)
    kapt(PodLib.hiltCompiler)
}