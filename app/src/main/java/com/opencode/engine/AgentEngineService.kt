package com.opencode.engine

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.opencode.App

class AgentEngineService : Service() {

    companion object {
        const val TAG = "AgentEngineService"
        const val NOTIFICATION_ID = 1001
        const val ACTION_START_ENGINE = "com.opencode.action.START_ENGINE"
        const val ACTION_STOP_ENGINE = "com.opencode.action.STOP_ENGINE"
        const val ACTION_PAUSE_ENGINE = "com.opencode.action.PAUSE_ENGINE"
        const val ACTION_RESUME_ENGINE = "com.opencode.action.RESUME_ENGINE"
        const val ACTION_NOTIFY_PERMISSION = "com.opencode.action.NOTIFY_PERMISSION"
        const val ACTION_NOTIFY_TASK_COMPLETE = "com.opencode.action.NOTIFY_TASK_COMPLETE"

        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_PERMITTED = "extra_permitted"
    }

    private val binder = EngineBinder()

    inner class EngineBinder : Binder() {
        fun getService(): AgentEngineService = this@AgentEngineService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_ENGINE -> {
                startForeground(NOTIFICATION_ID, buildEngineNotification("Engine running..."))
            }
            ACTION_STOP_ENGINE -> {
                @Suppress("DEPRECATION")
                stopForeground(true)
                stopSelf()
            }
            ACTION_PAUSE_ENGINE -> {
                val notification = buildEngineNotification("Engine paused")
                val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(NOTIFICATION_ID, notification)
            }
            ACTION_RESUME_ENGINE -> {
                val notification = buildEngineNotification("Engine running...")
                val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(NOTIFICATION_ID, notification)
            }
            ACTION_NOTIFY_PERMISSION -> {
                val title = intent.getStringExtra(EXTRA_TITLE) ?: "Permission"
                val message = intent.getStringExtra(EXTRA_MESSAGE) ?: ""
                val permitted = intent.getBooleanExtra(EXTRA_PERMITTED, false)
                val color = if (permitted) android.graphics.Color.GREEN else android.graphics.Color.RED
                val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(
                    NOTIFICATION_ID + 1,
                    NotificationCompat.Builder(this, App.CHANNEL_CODING_EVENTS)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setColor(color)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .build()
                )
            }
            ACTION_NOTIFY_TASK_COMPLETE -> {
                val title = intent.getStringExtra(EXTRA_TITLE) ?: "Task Complete"
                val message = intent.getStringExtra(EXTRA_MESSAGE) ?: ""
                val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(
                    NOTIFICATION_ID + 2,
                    NotificationCompat.Builder(this, App.CHANNEL_CODING_EVENTS)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .build()
                )
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        @Suppress("DEPRECATION")
        stopForeground(true)
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_ID)
        super.onDestroy()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val engineChannel = NotificationChannel(
            App.CHANNEL_ENGINE_RUNNING,
            "Engine Running",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Persistent notification when the coding engine is running"
            setShowBadge(false)
        }

        val eventsChannel = NotificationChannel(
            App.CHANNEL_CODING_EVENTS,
            "Coding Events",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for coding tasks and events"
            setShowBadge(true)
        }

        nm.createNotificationChannel(engineChannel)
        nm.createNotificationChannel(eventsChannel)
    }

    private fun buildEngineNotification(text: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            packageManager?.getLaunchIntentForPackage(packageName)?.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, App.CHANNEL_ENGINE_RUNNING)
            .setContentTitle("OpenCode Engine")
            .setContentText(text)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentIntent(pendingIntent)
            .build()
    }
}
