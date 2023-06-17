import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    kotlin("multiplatform")
    id("com.android.library")
//    id("io.realm.kotlin")
    kotlin("plugin.serialization") version libs.versions.serialization.get()
    id("com.codingfeline.buildkonfig") version libs.versions.buildKonfig.get()
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
            }
        }
        val androidTest by getting
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

buildkonfig {
    packageName = "com.mak.pocketnotes"
    objectName = "PNConfig"
    exposeObjectWithName = "PNPublicConfig"

    // default config is required
    defaultConfigs {
        buildConfigField(FieldSpec.Type.BOOLEAN, "isProd", "true")
    }
    // flavor is passed as a first argument of defaultConfigs
    defaultConfigs("mock") {
        buildConfigField(FieldSpec.Type.BOOLEAN, "isProd", "false")
    }
    defaultConfigs("prod") {
//        buildConfigField(FieldSpec.Type.BOOLEAN, "isProd", "true")
    }

//    targetConfigs {
//        create("android") {
//            buildConfigField(STRING, "name2", "value2")
//        }
//
//        create("ios") {
//            buildConfigField(STRING, "name", "valueIos")
//        }
//    }
    // flavor is passed as a first argument of targetConfigs
//    targetConfigs("dev") {
//        create("ios") {
//            buildConfigField(STRING, "name", "devValueIos")
//        }
//    }
}



android {
    namespace = "com.mak.pocketnotes"
    compileSdk = Integer.parseInt(libs.versions.compileSdk.get())
    defaultConfig {
        minSdk = Integer.parseInt(libs.versions.minSdk.get())
        targetSdk = Integer.parseInt(libs.versions.targetSdk.get())
    }
}