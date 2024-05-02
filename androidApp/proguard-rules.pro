# This is a configuration file for R8

-verbose
-allowaccessmodification
-repackageclasses

# Note that you cannot just include these flags in your own
# configuration file; if you are including this file, optimization
# will be turned off. You'll need to either edit this file, or
# duplicate the contents of this file and remove the include of this
# file from your project's proguard.config path property.

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# We only need to keep ComposeView
-keep public class androidx.compose.ui.platform.ComposeView {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# For enumeration classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# AndroidX + support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn androidx.**

-keepattributes SourceFile,
                LineNumberTable,
                RuntimeVisibleAnnotations,
                RuntimeVisibleParameterAnnotations,
                RuntimeVisibleTypeAnnotations,
                AnnotationDefault

-renamesourcefileattribute SourceFile

# Dagger
-dontwarn com.google.errorprone.annotations.*

# Retain the generic signature of retrofit2.Call until added to Retrofit.
# Issue: https://github.com/square/retrofit/issues/3580.
# Pull request: https://github.com/square/retrofit/pull/3579.
#-keep,allowobfuscation,allowshrinking class retrofit2.Call

# See https://issuetracker.google.com/issues/265188224
#-keep,allowshrinking class * extends androidx.compose.ui.node.ModifierNodeElement {}

# Using ktor client in Android has missing proguard rule
# See https://youtrack.jetbrains.com/issue/KTOR-5528

# This is generated automatically by the Android Gradle plugin.
#-dontwarn org.bouncycastle.jsse.BCSSLParameters
#-dontwarn org.bouncycastle.jsse.BCSSLSocket
#-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
#-dontwarn org.conscrypt.Conscrypt$Version
#-dontwarn org.conscrypt.Conscrypt
#-dontwarn org.conscrypt.ConscryptHostnameVerifier
#-dontwarn org.openjsse.javax.net.ssl.SSLParameters
#-dontwarn org.openjsse.javax.net.ssl.SSLSocket
#-dontwarn org.openjsse.net.ssl.OpenJSSE
#-dontwarn org.slf4j.**

## Keep `Companion` object fields of serializable classes.
## This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
#-if @kotlinx.serialization.Serializable class **
#-keepclassmembers class <1> {
#   static <1>$Companion Companion;
#}
#
## Keep `serializer()` on companion objects (both default and named) of serializable classes.
#-if @kotlinx.serialization.Serializable class ** {
#   static **$* *;
#}
#-keepclassmembers class <2>$<3> {
#   kotlinx.serialization.KSerializer serializer(...);
#}
#
## Keep `INSTANCE.serializer()` of serializable objects.
#-if @kotlinx.serialization.Serializable class ** {
#   public static ** INSTANCE;
#}
#-keepclassmembers class <1> {
#   public static <1> INSTANCE;
#   kotlinx.serialization.KSerializer serializer(...);
#}
#
## @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
#-keepattributes RuntimeVisibleAnnotations,AnnotationDefault