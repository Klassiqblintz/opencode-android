package com.opencode.bridge

import java.io.File

/**
 * Device bridge commands - for controlling the Android device
 * This is the "pc" CLI equivalent for the agent to control the device
 */
sealed class BridgeCommand {
    /** Check device bridge health */
    object Ping : BridgeCommand()

    /** Capture a screenshot */
    data class Shot(val path: String?) : BridgeCommand()

    /** Dump accessibility tree of current window */
    object Dump : BridgeCommand()

    /** Tap at screen coordinates */
    data class Tap(val x: Int, val y: Int) : BridgeCommand()

    /** Swipe on the screen */
    data class Swipe(
        val startX: Int, val startY: Int,
        val endX: Int, val endY: Int,
        val durationMs: Int = 300,
    ) : BridgeCommand()

    /** Type text into the focused input */
    data class Text(val text: String) : BridgeCommand()

    /** Press a system key */
    data class Key(val key: SystemKey) : BridgeCommand()

    /** Launch an app by package name */
    data class Open(val packageName: String) : BridgeCommand()

    /** Install an APK */
    data class Install(val apkPath: String) : BridgeCommand()

    /** Execute a shell command */
    data class Shell(val command: String) : BridgeCommand()

    /** List installed packages */
    data class ListPackages(val filter: String? = null) : BridgeCommand()

    /** Post a notification */
    data class Notify(val title: String, val message: String) : BridgeCommand()

    /** Run Gradle on-device */
    data class GradleOc(val args: String) : BridgeCommand()
}

enum class SystemKey {
    BACK,
    HOME,
    RECENT,
    VOLUME_UP,
    VOLUME_DOWN,
    POWER,
}
