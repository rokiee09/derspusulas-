# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.derspusulasi.data.model.**$$serializer { *; }
-keepclassmembers class com.derspusulasi.data.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.derspusulasi.data.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Jetpack Compose
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }

# Keep ViewModels
-keep class com.derspusulasi.ui.viewmodel.** { *; }

# Keep data classes for serialization
-keep class com.derspusulasi.data.model.** { *; }

# Keep all drawable resources
-keep class com.derspusulasi.R$drawable { *; }

# Keep all string resources
-keep class com.derspusulasi.R$string { *; }

# Keep all UI components
-keep class com.derspusulasi.ui.** { *; }

# Keep MainActivity
-keep class com.derspusulasi.MainActivity { *; }
