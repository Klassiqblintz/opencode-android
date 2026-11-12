# OpenCode Android ProGuard Rules
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Keep model classes
-keep class com.opencode.engine.** { *; }
-keep class com.opencode.storage.** { *; }
-keep class com.opencode.ui.** { *; }
-keep class com.opencode.app.** { *; }

# Keep Room entities
-keep class com.opencode.storage.entities.** { *; }
-keep class com.opencode.storage.dao.** { *; }

# Keep data classes
-keep class com.opencode.engine.** { *; }
-keep class com.opencode.utils.** { *; }

# Keep sealed classes and their subclasses
-keep class com.opencode.engine.** implements * { *; }

# Keep all data classes
-keep class **.$AutoValue_*
-keep class **._* { *; }
