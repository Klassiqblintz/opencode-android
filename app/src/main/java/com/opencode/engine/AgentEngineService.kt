package com.opencode.engine

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.opencode.R
import com.opencode.app.App

/**
 * Background service that keeps the coding agent engine running
 * even when the app is closed/foregrounded.
 *
 * Uses Foreground Service with a persistent notification.
 * User can toggle notifications on/off.
 *
 * The engine continues processing sessions, building apps,
 * running commands — all while the app UI may be closed.
 */
class AgentEngineService : Service() {

    private val binder = EngineBinder()
    private var isRunning = false

    companion object {
        const val TAG = "AgentEngineService"
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "opencode_engine_channel"

        // Action constants
        const val ACTION_START_ENGINE = "ACTION_START_ENGINE"
        const val ACTION_STOP_ENGINE = "ACTION_STOP_ENGINE"
        const val ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE"
        const val ACTION_BROADCAST_EVENT = "ACTION_BROADCAST_EVENT"

        // Event broadcast extras
        const val EXTRA_EVENT_TYPE = "event_type"
        const val EXTRA_SESSION_ID = "session_id"
        const val EXTRA_MESSAGE = "message"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_ENGINE -> {
                if (!isRunning) {
                    startForegroundService()
                    isRunning = true
                    Log.d(TAG, "Engine started")
                }
            }
            ACTION_STOP_ENGINE -> {
                if (isRunning) {
                    stopForeground(true)
                    stopSelf()
                    isRunning = false
                    Log.d(TAG, "Engine stopped")
                }
            }
            ACTION_SEND_MESSAGE -> {
                // Send message to active session
                val sessionId = intent.getStringExtra(EXTRA_SESSION_ID)
                val message = intent.getStringExtra(EXTRA_MESSAGE)
                Log.d(TAG, "Send message: session=$sessionId, msg=$message")
            }
            ACTION_BROADCAST_EVENT -> {
                // Broadcast event to app UI
                val eventType = intent.getStringExtra(EXTRA_EVENT_TYPE)
                val sessionId = intent.getStringExtra(EXTRA_SESSION_ID)
                val msg = intent.getStringExtra(EXTRA_MESSAGE)
                Log.d(TAG, "Broadcast event: type=$eventType, session=$sessionId")
            }
        }
        return START_STICKY  // Service restarts if killed
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        isRunning = false
        Log.d(TAG, "Service destroyed")
        super.onDestroy()
    }

    /**
     * Start the service as a foreground service with notification.
     * This keeps the engine alive even when the app is closed.
     */
    private fun startForegroundService() {
        val notification = createEngineNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    /**
     * Create the foreground notification for the engine service.
     * Shows when engine is running.
     */
    private fun createEngineNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_engine)  // We'll add this icon
            .setContentTitle("OpenCode Engine")
            .setContentText("Coding agent is running")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
    }

    /**
     * Create the notification channel for API 26+
     */
    private fun createNotificationChannel() {
        // Uses App.getNotificationManager()
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O {
            val manager = App.getNotificationManager(applicationContext)

            val channel = android.app.NotificationChannel(
                CHANNEL_ID,
                "OpenCode Engine",
                android.app.NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows when the coding agent engine is running in the background"
                setShowBadge(false)
            }

            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Show a notification for a specific event (session update, build complete, etc.)
     */
    fun showEventNotification(
        title: String,
        message: String,
        importance: Int = android.app.NotificationManager.IMPORTANCE_DEFAULT,
        onClickSessionId: String? = null
    ) {
        val manager = App.getNotificationManager(applicationContext)

        val channelId = if (importance > android.app.NotificationManager.IMPORTANCE_LOW) {
            "opencode_events_channel"
        } else {
            CHANNEL_ID
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val eventChannel = android.app.NotificationChannel(
                channelId,
                "OpenCode Events",
                importance.toNotificationChannelImportance()
            ).apply {
                description = "Notifications for coding agent events"
            }
            manager.createNotificationChannel(eventChannel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_engine)
            .setContentTitle("OpenCode")
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(if (importance >= android.app.NotificationManager.IMPORTANCE_HIGH)
                NotificationCompat.PRIORITY_HIGH
            else
                NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)

        if (onClickSessionId != null) {
            // PendingIntent to open the app to the relevant session
            // (will be set up properly in the Activity)
        }

        manager.notify(System.currentTimeMillis().toInt() and 0xFFFF, notification.build())
    }

    /**
     * Binder for Activity ↔ Service communication
     */
    inner class EngineBinder : Binder() {
        fun getService(): AgentEngineService = this@AgentEngineService
    }

    /**
     * Check if engine is currently running
     */
    fun isEngineRunning(): Boolean = isRunning
}

/**
 * Convert NotificationManager.importance to NotificationChannel.importance (API 26+)
 * Note: NotificationManager has no toNotificationImportance, so we map manually.
 * This is a Kotlin extension on Int.
 */
fun Int.toNotificationChannelImportance(): Int {
    return when (this) {
        android.app.NotificationManager.IMPORTANCE_HIGH -> android.app.NotificationManager.IMPORTANCE_HIGH
        android.app.NotificationManager.IMPORTANCE_DEFAULT -> android.app.NotificationManager.IMPORTANCE_DEFAULT
        android.app.NotificationManager.IMPORTANCE_LOW -> android.app.NotificationManager.IMPORTANCE_LOW
        android.app.NotificationManager.IMPORTANCE_MIN -> android.app.NotificationManager.IMPORTANCE_MIN
        else -> android.app.NotificationManager.IMPORTANCE_DEFAULT
    }
}
