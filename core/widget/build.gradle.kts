plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "com.mak.pocketnotes.core.widget"
  compileSdk = Integer.parseInt(libs.versions.compileSdk.get())

  defaultConfig {
    minSdk = Integer.parseInt(libs.versions.minSdk.get())
  }

  buildFeatures {
    compose = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
}

dependencies {
  implementation(project(":core:designsystem"))
  implementation(project(":core:database"))
  implementation(project(":core:common"))
  implementation(libs.compose.material)
  implementation(libs.androidx.glance.appwidget)
  implementation(libs.androidx.glance.material3)
  implementation(libs.androidx.media3.session)
  implementation(libs.koin.android)
}
