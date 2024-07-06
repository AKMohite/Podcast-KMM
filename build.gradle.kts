plugins {
    //trick: for the same plugin versions in all sub-modules
//    val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs") as org.gradle.accessors.dm.LibrariesForLibs
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
//    TODO how to apply kotlin plugin
    kotlin("android").version(libs.versions.kotlin.get()).apply(false)
    kotlin("multiplatform").version(libs.versions.kotlin.get()).apply(false)
}

subprojects {
//    region detekt
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
        config.setFrom(files("${rootProject.projectDir}/config/detekt/detekt.yml"))
//        baseline.set(file("${rootProject.projectDir}/config/detekt/baseline.xml"))
//        debug = true
        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
            xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
            txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
            sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with Github Code Scanning
            md.required.set(true) // simple Markdown format
        }
    }
//    endregion
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
