plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidKMMLibrary)
  alias(libs.plugins.skie)
  alias(libs.plugins.kotlinxSerialization)
}

kotlin {
  android {
    namespace = "com.mak.pocketnotes"
    compileSdk = Integer.parseInt(libs.versions.compileSdk.get())
    minSdk = Integer.parseInt(libs.versions.minSdk.get())

    // As per skill Path A point 11: Resolve Sub-dependency Variants
    localDependencySelection {
      selectBuildTypeFrom.set(listOf("debug", "release"))
    }
  }

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
    commonMain {
      compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
      }
      dependencies {
//                This override dependency and configurations exclude is required
//                for fixing some error in SKIE dependency. May not be need in
//                future.
        implementation(libs.stately.common)
        api(project(":core:common"))
        api(project(":core:remote"))
        api(project(":core:database"))
        api(project(":core:feature:domain"))
        api(project(":core:feature:data"))
//                implementation("co.touchlab:stately-collections:2.0.7")
//                implementation("co.touchlab:stately-concurrency:2.0.7")
//                implementation("co.touchlab:stately-isolate:2.0.7")
//                configurations.all {
//                    exclude(group = "co.touchlab", module = "stately-strict-jvm")
//                }

        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.ktor.serialization)
        // Use api so that the android app can use it as well
        implementation(libs.koin.core)
        implementation(libs.store5)
        implementation(libs.kotlinx.datetime)
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    androidMain {
      dependencies {
        implementation(libs.ktor.client.android)
        implementation(libs.koin.android)
        implementation(libs.androidx.media3.exoplayer)
        implementation(libs.androidx.media3.dash)
        implementation(libs.androidx.media3.ui)
        implementation(libs.androidx.media3.session)
        implementation(libs.kotlinx.coroutines.guava)
//                TODO can be removed?
        implementation(libs.androidx.legacy.support) // Needed MediaSessionCompat.Token
        implementation(libs.androidx.coil)
        implementation(libs.androidx.datastore.core)
        implementation(libs.androidx.datastore.tink)
      }
    }

    iosMain {
      dependencies {
      }
    }

    iosTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
