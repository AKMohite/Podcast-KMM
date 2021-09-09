buildscript {
    val compose_version by extra("1.0.0")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(PodClassPath.kotlinGradle)
        classpath(PodClassPath.buildGradle)
        classpath(PodClassPath.kotlinSerialization)
        classpath(PodClassPath.sqlDelight)
        classpath(PodClassPath.hiltGradle)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}