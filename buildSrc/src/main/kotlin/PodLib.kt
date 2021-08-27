object LibVersion {
    const val material = "1.4.0"
    const val appCompat = "1.3.1"
    const val constraintLayout = "2.1.0"
    const val coroutines = "1.5.0-native-mt"
    const val gradle = "7.1.0-alpha08"
    const val kotlin = "1.5.20"
    const val ktor = "1.6.1"
    const val serialization = "1.2.2"
    const val sqlDelight = "1.4.2"
}

object PodLib {
    const val material = "com.google.android.material:material:${LibVersion.material}"
    const val appCompat = "androidx.appcompat:appcompat:${LibVersion.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${LibVersion.constraintLayout}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibVersion.coroutines}"
    const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:${LibVersion.serialization}"
    const val ktorCore = "io.ktor:ktor-client-core:${LibVersion.ktor}"
    const val ktorAndroidClient = "io.ktor:ktor-client-android:${LibVersion.ktor}"
    const val ktoriOSClient = "io.ktor:ktor-client-ios:${LibVersion.ktor}"
    const val ktorSerialization = "io.ktor:ktor-client-serialization:${LibVersion.ktor}"
    const val sqlDelightRuntime = "com.squareup.sqldelight:runtime:${LibVersion.sqlDelight}"
    const val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:${LibVersion.sqlDelight}"
    const val sqlDelightNative = "com.squareup.sqldelight:native-driver:${LibVersion.sqlDelight}"
}

object PodClassPath {
    const val buildGradle = "com.android.tools.build:gradle:${LibVersion.gradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${LibVersion.kotlin}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${LibVersion.kotlin}"
    const val sqlDelight = "com.squareup.sqldelight:gradle-plugin:${LibVersion.sqlDelight}"
}