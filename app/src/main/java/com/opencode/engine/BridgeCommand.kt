package com.opencode.engine

// BridgeCommand: commands sent from the app to the engine
sealed class BridgeCommand {
    object Ping : BridgeCommand()
    data class Shot(val path: String? = null) : BridgeCommand()
    object Dump : BridgeCommand()
    data class Tap(val x: Int, val y: Int) : BridgeCommand()
    data class Swipe(
        val startX: Int,
        val startY: Int,
        val endX: Int,
        val endY: Int,
        val durationMs: Int = 300
    ) : BridgeCommand()
    data class Text(val text: String) : BridgeCommand()
    data class Key(val key: SystemKey) : BridgeCommand()
    data class Open(val packageName: String) : BridgeCommand()
}
