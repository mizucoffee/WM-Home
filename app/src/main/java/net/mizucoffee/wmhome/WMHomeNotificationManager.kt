package net.mizucoffee.wmhome

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.content.Intent.CATEGORY_LAUNCHER
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
import androidx.core.app.NotificationCompat

class WMHomeNotificationManager(
    private val notificationManager: NotificationManager,
    private val context: Context
) {
    companion object {
        private const val CHANNEL_ID = "WM-HOME"
        private const val NOTIFICATION_ID = 0
        private const val HOME_PACKAGE = "com.android.launcher3"
        private const val HOME_CLASS = "com.android.searchlauncher.SearchLauncher"
    }

    fun createNotificationChannel() {
        val name = "WM Home"
        val descriptionText = "ホーム画面を開くための通知を表示します"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun notifyHomeLink() {
        val i = Intent(ACTION_MAIN).also {
            it.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            it.addCategory(CATEGORY_LAUNCHER)
            it.component = ComponentName(HOME_PACKAGE, HOME_CLASS)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("WM Home")
            .setContentText("ホーム画面を表示")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun cancelHomeLink() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}