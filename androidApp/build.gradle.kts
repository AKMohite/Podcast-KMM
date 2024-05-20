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
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        create("staging") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".debugStaging"
//            TODO enable code obfuscation
//            isMinifyEnabled = true
//            isShrinkResources = true
            isDebuggable = true
            matchingFallbacks += listOf("debug")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.bundles.compose)
    implementation(libs.koin.android.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.material3.window.size)
    implementation(libs.accompanist.adaptive)

}