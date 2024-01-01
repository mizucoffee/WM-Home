package net.mizucoffee.wmhome

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class WMHomeAccessibilityService : AccessibilityService() {

    companion object {
        @JvmStatic
        private var sharedInstance: WMHomeAccessibilityService? = null

        fun getInstance() = sharedInstance
    }

    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onServiceConnected() {
        sharedInstance = this
        super.onServiceConnected()
    }

    override fun onUnbind(intent: Intent): Boolean {
        sharedInstance = null
        return super.onUnbind(intent)
    }

    fun back() {
        performGlobalAction(GLOBAL_ACTION_BACK)
    }
}