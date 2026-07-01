import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "app.mak.pocketnotes.wearos"
    compileSdk = Integer.parseInt(libs.versions.compileSdk.get())

    defaultConfig {
        applicationId = "app.mak.pocketnotes.wearos"
        minSdk = Integer.parseInt(libs.versions.wearMinSdk.get())
        targetSdk = Integer.parseInt(libs.versions.targetSdk.get())
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    useLibrary("wear-sdk")
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        optIn.add("com.google.android.horologist.annotations.ExperimentalHorologistApi")
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.androidx.wear.material3)
    implementation(libs.androidx.wear.ui.tooling)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.coil)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.wear.tooling.preview)
    implementation(libs.compose.ui.preview)
    implementation(libs.play.services.wearable)
    implementation(libs.horologist.media.ui)
    implementation(libs.horologist.media.ui.material3)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.test.junit4)
    debugImplementation(libs.compose.test.manifest)
    debugImplementation(libs.compose.ui.tooling)
}