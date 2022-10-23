plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.mak.pocketnotes.android"
    compileSdk = Integer.parseInt(libs.versions.compileSdk.get())
    defaultConfig {
        applicationId = "com.mak.pocketnotes.android"
        minSdk = Integer.parseInt(libs.versions.minSdk.get())
        targetSdk = Integer.parseInt(libs.versions.targetSdk.get())
        versionCode = Integer.parseInt(libs.versions.versionCode.get())
        versionName = libs.versions.versionName.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.bundles.compose)
}