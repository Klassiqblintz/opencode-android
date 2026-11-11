package com.opencode.app

import android.app.Application
import android.app.NotificationManager
import android.content.Context

/**
 * OpenCode Application - Universal Coding Agent Platform
 *
 * Supports multiple coding agents:
 * - OpenCode
 * - DeepSeek
 * - Claude Code
 * - Pi Agent
 * - Custom agents
 *
 * Each agent can run:
 * - On-device (pRoot Linux environment)
 * - Remote server connection
 *
 * Features:
 * - All OpenCode features: sessions, skills, MCP servers, agents, permissions, Code Mode
 * - Build all APK types: Flutter, Kotlin/Compose, Java/Gradle, Native C/C++
 * - File uploads: images, media, ZIPs, any file type
 * - Project management: import/export ZIPs, recent projects
 * - Notifications: push notifications for agent events
 * - Embedded terminal: xterm.js-style terminal for command execution
 * - Preview panel: WebView for web apps, native launch for compiled APKs
 * - Background service: engine survives app close
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @Volatile
        private var instance: App? = null

        fun getInstance(): App? = instance

        /**
         * Get the notification manager for the app
         */
        fun getNotificationManager(context: Context): NotificationManager {
            return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }
}
