# OpenCode Android - ProGuard/R8 rules
# Keep OpenCode-specific classes and annotations

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep class com.google.inject.** { *; }
-keep class * extends dagger.Component
-keep class * extends dagger.Module
-keep class * extends dagger.internal.Module
-keep class * extends dagger.Provides
-keepclassmembers,allowObsolete,class=** {
    @dagger.* <methods>;
}
-keepclassmembers class * {
    @dagger.* <init>(...);
}

# Room
-keep class com.opencode.storage.room.** { *; }
-keep class com.opencode.storage.entities.** { *; }
-keep class com.opencode.storage.dao.** { *; }
-keep class * extends androidx.room.RoomDatabase

# Kotlin
-keepattributes *Annotation*, InnerClasses
-keepclassmembers class * {
    @kotlin.Metadata *;
}
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# Coroutines
-keep class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keep class kotlinx.coroutines.internal.MainDispatchersWrappingKt {}
-keep class kotlinx.coroutines.CoroutineExceptionHandler {}
-keep class kotlinx.coroutines.Dispatchers {}
-keep class kotlinx.coroutines.MainThreadExecutor {}
-keep class kotlinx.coroutines.android.HandlerDispatcher {}

# Serialization
-keep class com.opencode.engine.** { *; }
-keep class com.opencode.bridge.** { *; }
-keep class com.opencode.utils.** { *; }

# Retain data classes (for serialization and Room)
-keep class com.opencode.engine.AgentType { *; }
-keep class com.opencode.engine.AgentConfig { *; }
-keep class com.opencode.engine.ModelConfig { *; }
-keep class com.opencode.engine.AgentRunMode { *; }
-keep class com.opencode.engine.ModelSource { *; }
-keep class com.opencode.bridge.BridgeCommand { *; }
-keep class com.opencode.bridge.SystemKey { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# WebView (for preview)
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-webkit WebChromeClient

# xterm.js placeholder (if used)
# -keep class org.nhind.** { *; }

# OkHttp
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Coil (image loading)
-keep class coil.** { *; }
-keep class coil2.** { *; }

# Apache Commons Compress (for ZIP)
-keep class org.apache.commons.compress.** { *; }

# Biometric
-keep class androidx.biometric.** { *; }

# Remove unused code
# -assumenosideeffects class android.util.Log {
#     public static int v(...);
#     public static int d(...);
#     public static int i(...);
#     public static int w(...);
#     public static int e(...);
# }
