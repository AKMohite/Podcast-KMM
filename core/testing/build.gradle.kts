plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidKMMLibrary)
}

kotlin {
  android {
    namespace = "com.mak.pocketnotes.core.testing"
    compileSdk = Integer.parseInt(libs.versions.compileSdk.get())
    minSdk = Integer.parseInt(libs.versions.minSdk.get())

    withHostTestBuilder {
    }

    withDeviceTestBuilder {
      sourceSetTreeName = "test"
    }.configure {
      instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  }

  val xcfName = "core:testingKit"

  iosX64 {
    binaries.framework {
      baseName = xcfName
    }
  }

  iosArm64 {
    binaries.framework {
      baseName = xcfName
    }
  }

  iosSimulatorArm64 {
    binaries.framework {
      baseName = xcfName
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":core:common"))
        implementation(project(":core:feature:domain"))
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.test)
        implementation(libs.androidx.paging.common)
        implementation(libs.turbine)
      }
    }

    commonTest {
      dependencies {
      }
    }

    androidMain {
      dependencies {
        implementation(libs.junit4)
      }
    }
  }
}
