plugins {
    kotlin("multiplatform")
    id("com.android.library")
//    id("io.realm.kotlin")
    kotlin("plugin.serialization") version libs.versions.serialization.get()
}

kotlin {
    android()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.content.negotiation)
                implementation(libs.ktor.serialization)
//                implementation(libs.realm.base)
                //Use api so that the android app can use it as well
                implementation(libs.koin.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
                implementation(libs.koin.android)
                implementation(libs.androidx.media3.exoplayer)
                implementation(libs.androidx.media3.dash)
                implementation(libs.androidx.media3.ui)
                implementation(libs.androidx.media3.session)
//                TODO can be removed?
                implementation("androidx.legacy:legacy-support-v4:1.0.0") // Needed MediaSessionCompat.Token
                implementation(libs.androidx.coil)
            }
        }
//        val androidUnitTest by getting
//        val androidAndroidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.ios)
            }
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.mak.pocketnotes"
    compileSdk = Integer.parseInt(libs.versions.compileSdk.get())
    defaultConfig {
        minSdk = Integer.parseInt(libs.versions.minSdk.get())
        targetSdk = Integer.parseInt(libs.versions.targetSdk.get())
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}