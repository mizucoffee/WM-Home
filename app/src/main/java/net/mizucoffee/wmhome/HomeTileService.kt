package net.mizucoffee.wmhome

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.service.quicksettings.TileService
import kotlinx.coroutines.runBlocking

class HomeTileService : TileService() {
    private val homeDataStore = HomeDataStore(this)

    override fun onTileAdded() {
        super.onTileAdded()
        val wmHomeNotificationManager = WMHomeNotificationManager(
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
            this
        )

        runBlocking {
            homeDataStore.setIsTileSet(true)
        }
        wmHomeNotificationManager.cancelHomeLink()
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        val wmHomeNotificationManager = WMHomeNotificationManager(
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
            this
        )

        runBlocking {
            homeDataStore.setIsTileSet(false)
        }
        wmHomeNotificationManager.notifyHomeLink()
    }

    override fun onClick() {
        super.onClick()
        Intent(Intent.ACTION_MAIN).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            it.addCategory(Intent.CATEGORY_LAUNCHER)
            it.component =
                ComponentName("com.android.launcher3", "com.android.searchlauncher.SearchLauncher")
        }.let { startActivity(it) }
    }
}