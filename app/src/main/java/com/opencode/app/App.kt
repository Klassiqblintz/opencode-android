package com.opencode.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.opencode.engine.AgentEngineService

class App : android.app.Application() {

    companion object {
        const val CHANNEL_ENGINE_RUNNING = "opencode_engine_running"
        const val CHANNEL_CODING_EVENTS = "opencode_coding_events"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Persistent notification channel for engine running state
        val engineChannel = NotificationChannel(
            CHANNEL_ENGINE_RUNNING,
            "Engine Running",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Persistent notification when the coding engine is running"
            setShowBadge(false)
            lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
        }

        // Task notification channel for coding events
        val eventsChannel = NotificationChannel(
            CHANNEL_CODING_EVENTS,
            "Coding Events",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for coding tasks and events"
            setShowBadge(true)
            lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
        }

        notificationManager.createNotificationChannel(engineChannel)
        notificationManager.createNotificationChannel(eventsChannel)
    }
}
