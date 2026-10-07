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
        // প্রয়োজন হলে এখানে UI/event monitoring যোগ করা যাবে।
    }

    override fun onInterrupt() {
        // Accessibility interrupted.
    }

    override fun onDestroy() {

        instance = null

        super.onDestroy()
    }

    fun swipeUp() {

        val displayMetrics = resources.displayMetrics

        val width = displayMetrics.widthPixels.toFloat()
        val height = displayMetrics.heightPixels.toFloat()

        val x = width / 2f

        val startY = height * 0.75f
        val endY = height * 0.25f

        performSwipe(
            x,
            startY,
            x,
            endY
        )
    }

    fun swipeDown() {

        val displayMetrics = resources.displayMetrics

        val width = displayMetrics.widthPixels.toFloat()
        val height = displayMetrics.heightPixels.toFloat()

        val x = width / 2f

        val startY = height * 0.25f
        val endY = height * 0.75f

        performSwipe(
            x,
            startY,
            x,
            endY
        )
    }

    private fun performSwipe(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float
    ) {

        val path = Path()

        path.moveTo(startX, startY)
        path.lineTo(endX, endY)

        val gesture =
            GestureDescription.Builder()
                .addStroke(
                    GestureDescription.StrokeDescription(
                        path,
                        0,
                        400
                    )
                )
                .build()

        dispatchGesture(
            gesture,
            null,
            null
        )
    }
}
