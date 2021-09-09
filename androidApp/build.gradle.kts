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
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = LibVersion.compose
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(PodModule.shared))
    implementation(PodLib.material)
    implementation(PodLib.appCompat)
    implementation(PodLib.hiltAndroid)
    implementation(PodLib.hiltNavigation)
    kapt(PodLib.hiltCompiler)

    implementation(PodLib.composeRuntime)
    implementation(PodLib.runtimeLiveData)
    implementation(PodLib.composeUI)
    implementation(PodLib.materialCompose)
    implementation(PodLib.uiTooling)
    implementation(PodLib.composeFoundation)
    implementation(PodLib.composeCompiler)
    implementation(PodLib.constraintLayoutCompose)
    implementation(PodLib.composeActivity)
    implementation(PodLib.composeNavigation)
}