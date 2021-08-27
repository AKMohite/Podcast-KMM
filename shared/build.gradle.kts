import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

version = "1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        else -> ::iosX64
    }

    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "shared"
        podfile = project.file("../iosApp/Podfile")
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(PodLib.coroutines)
                implementation(PodLib.kotlinSerialization)
                implementation(PodLib.ktorCore)
                implementation(PodLib.ktorSerialization)
                implementation(PodLib.sqlDelightRuntime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(PodLib.ktorAndroidClient)
                implementation(PodLib.sqlDelightAndroid)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(PodLib.ktoriOSClient)
                implementation(PodLib.sqlDelightNative)
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdk = PodConfig.compileSDKVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = PodConfig.minSdkVersion
        targetSdk = PodConfig.targetSdkVersion
    }
}