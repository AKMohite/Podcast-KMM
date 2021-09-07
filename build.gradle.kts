buildscript {
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