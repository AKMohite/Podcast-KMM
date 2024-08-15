plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("app.cash.sqldelight") version libs.versions.sqldelight
    id("co.touchlab.skie") version libs.versions.skie
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
}

kotlin {
    androidTarget()
    
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
//                This override dependency and configurations exclude is required
//                for fixing some error in SKIE dependency. May not be need in
//                future.
                implementation("co.touchlab:stately-common:2.0.7")
//                implementation("co.touchlab:stately-collections:2.0.7")
//                implementation("co.touchlab:stately-concurrency:2.0.7")
//                implementation("co.touchlab:stately-isolate:2.0.7")
//                configurations.all {
//                    exclude(group = "co.touchlab", module = "stately-strict-jvm")
//                }


                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.content.negotiation)
                implementation(libs.ktor.serialization)
                //Use api so that the android app can use it as well
                implementation(libs.koin.core)
                implementation(libs.sqldelight.extensions)
                implementation(libs.sqldelight.primitive)
                implementation(libs.store5)
                implementation(libs.kotlinx.datetime)
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
                implementation(libs.androidx.legacy.support) // Needed MediaSessionCompat.Token
                implementation(libs.androidx.coil)
                implementation(libs.android.sql.driver)
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
                implementation(libs.ios.sql.driver)
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("PocketDatabase") {
            packageName.set("com.mak.pocketnotes")
        }
    }
}