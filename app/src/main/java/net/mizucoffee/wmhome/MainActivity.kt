package net.mizucoffee.wmhome

import android.app.NotificationManager
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.content.Intent.CATEGORY_LAUNCHER
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private val homeDataStore = HomeDataStore(this)

    companion object {
        private const val W_MUSIC_PACKAGE = "com.sony.walkman.highresmediaplayer"
        private const val W_MUSIC_CLASS = "$W_MUSIC_PACKAGE.activity.LauncherActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wmHomeNotificationManager = WMHomeNotificationManager(
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
            this
        )

        val usm = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val appList = usm.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - 1000 * 1000,
            time
        ).sortedByDescending { it.lastTimeUsed }.subList(0, 1)

        wmHomeNotificationManager.createNotificationChannel()

        if (!runBlocking { homeDataStore.getIsTileSet() }) {
            wmHomeNotificationManager.notifyHomeLink()
        } else {
            wmHomeNotificationManager.cancelHomeLink()
        }

        val i = Intent(ACTION_MAIN).also {
            it.flags = FLAG_ACTIVITY_NEW_TASK or
                    FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            it.addCategory(CATEGORY_LAUNCHER)
            it.component = ComponentName(W_MUSIC_PACKAGE, W_MUSIC_CLASS)
        }
        startActivity(i)

        if (appList.map { it.packageName }.contains(W_MUSIC_PACKAGE)) {
            runBlocking {
                WMHomeAccessibilityService.getInstance()?.back()
            }
        }
        finish()
    }
}
