package com.opencode.utils

// Project types supported by the build system
enum class ProjectType(
    val displayName: String,
    val buildCommand: String
) {
    FLUTTER("Flutter", "flutter build apk --release"),
    KOTLIN_COMPOSE("Kotlin / Compose", "./gradlew assembleRelease"),
    JAVA_GRADLE("Java / Gradle", "./gradlew assembleRelease"),
    NATIVE_C_CPP("Native C/C++", "cmake --build ."),
    MIXED("Mixed / Unknown", "");

    companion object {
        fun fromString(value: String): ProjectType {
            return entries.find { it.name.equals(value, ignoreCase = true) }
                ?: MIXED
        }
    }
}

// Detects project type by scanning directory contents
object ProjectTypeDetector {

    fun detectType(directory: String): ProjectType {
        val scores = mutableMapOf<ProjectType, Int>()
        val files = scanDirectory(directory)

        // Flutter scoring
        var flutterScore = 0
        if (files.contains("pubspec.yaml")) flutterScore += 3
        if (files.contains("pubspec.yml")) flutterScore += 3
        if (hasDirectory(directory, "lib")) flutterScore += 1
        scores[ProjectType.FLUTTER] = flutterScore

        // Gradle scoring
        var gradleScore = 0
        if (files.contains("settings.gradle")) gradleScore += 2
        if (files.contains("settings.gradle.kts")) gradleScore += 2
        if (files.contains("build.gradle")) gradleScore += 1
        if (files.contains("build.gradle.kts")) gradleScore += 1
        if (hasFile(directory, "gradlew")) gradleScore += 1
        if (hasFile(directory, "gradlew.bat")) gradleScore += 1
        scores[ProjectType.JAVA_GRADLE] = gradleScore
        scores[ProjectType.KOTLIN_COMPOSE] = gradleScore

        // Kotlin scoring
        var kotlinScore = 0
        files.filter { it.endsWith(".kt") || it.endsWith(".kts") }.forEach { kotlinScore++ }
        scores[ProjectType.KOTLIN_COMPOSE] = (scores[ProjectType.KOTLIN_COMPOSE] ?: 0) + kotlinScore

        // Java scoring
        var javaScore = 0
        files.filter { it.endsWith(".java") }.forEach { javaScore++ }
        scores[ProjectType.JAVA_GRADLE] = (scores[ProjectType.JAVA_GRADLE] ?: 0) + javaScore

        // Native C/C++ scoring
        var nativeScore = 0
        if (files.contains("CMakeLists.txt")) nativeScore += 2
        if (files.contains("Android.mk")) nativeScore += 2
        files.filter { it.endsWith(".cpp") || it.endsWith(".c") || it.endsWith(".h") || it.endsWith(".hpp") }
            .forEach { nativeScore++ }
        scores[ProjectType.NATIVE_C_CPP] = nativeScore

        // Determine result
        val nonZeroScores = scores.filter { it.value > 0 }
        if (nonZeroScores.size > 1) {
            // Multiple types detected → mixed
            return ProjectType.MIXED
        }

        return nonZeroScores.entries.maxByOrNull { it.value }?.key ?: ProjectType.MIXED
    }

    private fun scanDirectory(dir: String): Set<String> {
        val file = java.io.File(dir)
        if (!file.isDirectory) return emptySet()
        val allFiles = mutableSetOf<String>()
        fun walk(d: java.io.File) {
            d.listFiles()?.forEach {
                if (it.isFile) allFiles.add(it.name)
                if (it.isDirectory) walk(it)
            }
        }
        walk(file)
        return allFiles
    }

    private fun hasFile(dir: String, name: String): Boolean {
        return java.io.File(dir, name).exists()
    }

    private fun hasDirectory(dir: String, name: String): Boolean {
        return java.io.File(dir, name).isDirectory
    }
}
