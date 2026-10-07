package com.example.voiceassistant

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class VoiceAccessibilityService : AccessibilityService() {

    companion object {
        var instance: VoiceAccessibilityService? = null
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {
        // এখানে প্রয়োজনীয় accessibility event পাওয়া যাবে।
    }

    override fun onInterrupt() {
        // Service interrupted.
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    fun goBack(): Boolean {
        return performGlobalAction(
            GLOBAL_ACTION_BACK
        )
    }

    fun goHome(): Boolean {
        return performGlobalAction(
            GLOBAL_ACTION_HOME
        )
    }

    fun openRecents(): Boolean {
        return performGlobalAction(
            GLOBAL_ACTION_RECENTS
        )
    }

    fun swipeUp() {

        val metrics = resources.displayMetrics

        val width = metrics.widthPixels.toFloat()
        val height = metrics.heightPixels.toFloat()

        val x = width / 2f

        swipe(
            x,
            height * 0.78f,
            x,
            height * 0.22f
        )
    }

    fun swipeDown() {

        val metrics = resources.displayMetrics

        val width = metrics.widthPixels.toFloat()
        val height = metrics.heightPixels.toFloat()

        val x = width / 2f

        swipe(
            x,
            height * 0.22f,
            x,
            height * 0.78f
        )
    }

    private fun swipe(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float
    ) {

        val path = Path()

        path.moveTo(
            startX,
            startY
        )

        path.lineTo(
            endX,
            endY
        )

        val stroke =
            GestureDescription.StrokeDescription(
                path,
                0,
                450
            )

        val gesture =
            GestureDescription.Builder()
                .addStroke(stroke)
                .build()

        dispatchGesture(
            gesture,
            null,
            null
        )
    }
}
