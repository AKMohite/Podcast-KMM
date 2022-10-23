plugins {
    //trick: for the same plugin versions in all sub-modules
//    val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs") as org.gradle.accessors.dm.LibrariesForLibs
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.library) apply false
//    TODO how to apply kotlin plugin
    kotlin("android").version(libs.versions.kotlin.get()).apply(false)
    kotlin("multiplatform").version(libs.versions.kotlin.get()).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
