package com.opencode.utils

import java.io.File

/**
 * Project type auto-detection
 * Determines what kind of project is in a given directory
 */
object ProjectTypeDetector {

    /**
     * Detect the project type from a directory path
     * Returns the most specific type found
     */
    fun detectType(directory: String): ProjectType {
        val dir = File(directory)
        if (!dir.exists() || !dir.isDirectory) return ProjectType.MIXED

        var scores = mutableMapOf<ProjectType, Int>()

        // Check for Flutter
        if (File("$directory/pubspec.yaml").exists()) {
            scores[ProjectType.FLUTTER] = scores.getOrElse(ProjectType.FLUTTER) { 0 } + 3
        }
        if (File("$directory/lib").exists()) {
            scores[ProjectType.FLUTTER] = scores.getOrElse(ProjectType.FLUTTER) { 0 } + 1
        }

        // Check for Gradle-based projects
        if (File("$directory/settings.gradle").exists() ||
            File("$directory/settings.gradle.kts").exists()) {
            scores[ProjectType.KOTLIN_COMPOSE] = scores.getOrElse(ProjectType.KOTLIN_COMPOSE) { 0 } + 2
            scores[ProjectType.JAVA_GRADLE] = scores.getOrElse(ProjectType.JAVA_GRADLE) { 0 } + 2
        }
        if (File("$directory/build.gradle").exists() ||
            File("$directory/build.gradle.kts").exists()) {
            scores[ProjectType.KOTLIN_COMPOSE] = scores.getOrElse(ProjectType.KOTLIN_COMPOSE) { 0 } + 1
            scores[ProjectType.JAVA_GRADLE] = scores.getOrElse(ProjectType.JAVA_GRADLE) { 0 } + 1
        }
        if (File("$directory/gradlew").exists()) {
            scores[ProjectType.KOTLIN_COMPOSE] = scores.getOrElse(ProjectType.KOTLIN_COMPOSE) { 0 } + 1
            scores[ProjectType.JAVA_GRADLE] = scores.getOrElse(ProjectType.JAVA_GRADLE) { 0 } + 1
        }

        // Check for Kotlin
        val ktFiles = dir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .count()
        if (ktFiles > 0) {
            scores[ProjectType.KOTLIN_COMPOSE] = scores.getOrElse(ProjectType.KOTLIN_COMPOSE) { 0 } + ktFiles
        }

        // Check for Java
        val javaFiles = dir.walkTopDown()
            .filter { it.isFile && it.extension == "java" }
            .count()
        if (javaFiles > 0) {
            scores[ProjectType.JAVA_GRADLE] = scores.getOrElse(ProjectType.JAVA_GRADLE) { 0 } + javaFiles
        }

        // Check for Native C/C++
        if (File("$directory/CMakeLists.txt").exists() ||
            File("$directory/Android.mk").exists()) {
            scores[ProjectType.NATIVE_C_CPP] = scores.getOrElse(ProjectType.NATIVE_C_CPP) { 0 } + 2
        }
        val cppFiles = dir.walkTopDown()
            .filter { it.isFile && setOf("cpp", "c", "h", "hpp").contains(it.extension) }
            .count()
        if (cppFiles > 0) {
            scores[ProjectType.NATIVE_C_CPP] = scores.getOrElse(ProjectType.NATIVE_C_CPP) { 0 } + cppFiles
        }

        // Check for mixed
        if (scores.keys.size > 1) {
            return ProjectType.MIXED
        }

        // Return highest score
        return scores.maxByOrNull { it.value }?.key ?: ProjectType.MIXED
    }
}
