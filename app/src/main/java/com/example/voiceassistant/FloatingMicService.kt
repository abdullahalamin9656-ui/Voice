package com.example.voiceassistant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast

class FloatingMicService : Service() {

    private lateinit var windowManager: WindowManager

    private var micButton: ImageButton? = null

    override fun onCreate() {

        super.onCreate()

        createNotificationChannel()

        startForeground(
            1001,
            createNotification()
        )

        showFloatingButton()
    }

    private fun showFloatingButton() {

        windowManager =
            getSystemService(WINDOW_SERVICE) as WindowManager

        micButton = ImageButton(this).apply {

            setBackgroundColor(Color.DKGRAY)

            setImageResource(
                android.R.drawable.ic_btn_speak_now
            )

            setOnClickListener {

                Toast.makeText(
                    this@FloatingMicService,
                    "মাইক্রোফোন প্রস্তুত",
                    Toast.LENGTH_SHORT
                ).show()

                // পরের ধাপে SpeechRecognizer যুক্ত করা হবে।
            }
        }

        val windowType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

        val params = WindowManager.LayoutParams(
            150,
            150,
            windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity =
            Gravity.END or Gravity.CENTER_VERTICAL

        params.x = 20
        params.y = 0

        windowManager.addView(
            micButton,
            params
        )
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "voice_assistant",
                "Voice Assistant",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager =
                getSystemService(
                    NotificationManager::class.java
                )

            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification.Builder(
                this,
                "voice_assistant"
            )
                .setContentTitle("Voice Assistant")
                .setContentText("Floating microphone চালু আছে")
                .setSmallIcon(
                    android.R.drawable.ic_btn_speak_now
                )
                .build()

        } else {

            Notification.Builder(this)
                .setContentTitle("Voice Assistant")
                .setContentText("Floating microphone চালু আছে")
                .setSmallIcon(
                    android.R.drawable.ic_btn_speak_now
                )
                .build()
        }
    }

    override fun onDestroy() {

        micButton?.let {
            windowManager.removeView(it)
        }

        micButton = null

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
