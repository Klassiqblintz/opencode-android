package com.opencode.utils

/**
 * Project types supported by OpenCode Android.
 * Used for auto-detection, build configuration, and UI display.
 */
enum class ProjectType(
    val displayName: String,
    val buildCommand: String = "",
) {
    FLUTTER(
        displayName = "Flutter",
        buildCommand = "flutter build apk --release"
    ),
    KOTLIN_COMPOSE(
        displayName = "Kotlin / Compose",
        buildCommand = "./gradlew assembleRelease"
    ),
    JAVA_GRADLE(
        displayName = "Java / Gradle",
        buildCommand = "./gradlew assembleRelease"
    ),
    NATIVE_C_CPP(
        displayName = "Native C/C++",
        buildCommand = "cmake --build ."
    ),
    MIXED(
        displayName = "Mixed / Unknown",
        buildCommand = ""
    );

    companion object {
        fun fromString(value: String): ProjectType {
            return try {
                valueOf(value.uppercase())
            } catch (e: Exception) {
                MIXED
            }
        }
    }
}
