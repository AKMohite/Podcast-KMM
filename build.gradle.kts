plugins {
  alias(libs.plugins.androidApplication).apply(false)
  alias(libs.plugins.androidLibrary).apply(false)
  alias(libs.plugins.androidKMMLibrary).apply(false)
  alias(libs.plugins.kotlinMultiplatform).apply(false)
  alias(libs.plugins.compose.compiler).apply(false)
  alias(libs.plugins.detekt).apply(false)
  alias(libs.plugins.android.test).apply(false)
  alias(libs.plugins.baselineprofile).apply(false)
  alias(libs.plugins.kotlinxSerialization).apply(false)
  alias(libs.plugins.sqldelight).apply(false)
  alias(libs.plugins.skie).apply(false)
  alias(libs.plugins.spotless).apply(false)
}

allprojects {
  pluginManager.apply(
    rootProject.libs.plugins.spotless
      .get()
      .pluginId
  )

  extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
      target("**/*.kt")
      targetExclude("**/build/**/*.kt", "**/devastation/**/*.kt")
      ktlint(libs.versions.ktlint.get()).editorConfigOverride(
        mapOf(
          "ktlint_code_style" to "android_studio",
          "ktlint_function_naming_ignore_when_annotated_with" to "Composable"
        )
      )
    }
    kotlinGradle {
      target("*.gradle.kts")
      ktlint(libs.versions.ktlint.get()).editorConfigOverride(
        mapOf(
          "ktlint_code_style" to "android_studio"
        )
      )
    }
    format("xml") {
      target("**/*.xml")
      targetExclude("**/build/**/*.xml", "**/devastation/**/*.xml")
      leadingTabsToSpaces()
      trimTrailingWhitespace()
      endWithNewline()
    }
    format("misc") {
      target("**/*.md", "**/*.yaml", "**/*.yml", "**/*.properties", "**/*.json", "**/.gitignore")
      targetExclude("**/build/**", "**/devastation/**")
      leadingTabsToSpaces()
      trimTrailingWhitespace()
      endWithNewline()
    }
  }

  // Disable automatic check during build
  tasks.whenTaskAdded {
    if (name == "spotlessCheck") {
      enabled = false
    }
  }
}

subprojects {
  pluginManager.apply(
    rootProject.libs.plugins.detekt
      .get()
      .pluginId
  )

  tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    exclude("**/devastation/**")
    config.setFrom(files("${rootProject.projectDir}/config/detekt/detekt.yml"))
    reports {
      html.required.set(true)
      xml.required.set(true)
      txt.required.set(true)
      sarif.required.set(true)
      md.required.set(true)
    }
  }
}

tasks.named("clean", Delete::class) {
  delete(rootProject.layout.buildDirectory)
}
