package com.example.voiceassistant

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat

class FloatingMicService : Service() {

    private lateinit var windowManager: WindowManager

    private var micButton: ImageButton? = null

    private var voiceRecognizer: VoiceRecognizer? = null

    private var assistantController: AssistantController? = null

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        startForeground(
            1001,
            createNotification()
        )

        assistantController =
            AssistantController(this)

        voiceRecognizer =
            VoiceRecognizer(
                context = this,

                onResult = { text ->

                    assistantController?.processVoice(text)

                    Toast.makeText(
                        this,
                        "আপনি বলেছেন: $text",
                        Toast.LENGTH_SHORT
                    ).show()
                },

                onError = { message ->

                    Toast.makeText(
                        this,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

        showFloatingButton()
    }

    private fun showFloatingButton() {

        windowManager =
            getSystemService(
                WINDOW_SERVICE
            ) as WindowManager

        micButton =
            ImageButton(this).apply {

                setBackgroundColor(
                    Color.DKGRAY
                )

                setImageResource(
                    android.R.drawable.ic_btn_speak_now
                )

                setColorFilter(
                    Color.WHITE
                )

                setOnClickListener {

                    startVoiceRecognition()
                }
            }

        val windowType =
            if (
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O
            ) {
                WindowManager.LayoutParams
                    .TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams
                    .TYPE_PHONE
            }

        val params =
            WindowManager.LayoutParams(
                160,
                160,
                windowType,

                WindowManager.LayoutParams
                    .FLAG_NOT_FOCUSABLE,

                PixelFormat.TRANSLUCENT
            )

        params.gravity =
            Gravity.END or
                    Gravity.CENTER_VERTICAL

        params.x = 20

        params.y = 0

        windowManager.addView(
            micButton,
            params
        )
    }

    private fun startVoiceRecognition() {

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            Toast.makeText(
                this,
                "Microphone permission প্রয়োজন",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        Toast.makeText(
            this,
            "বলুন...",
            Toast.LENGTH_SHORT
        ).show()

        voiceRecognizer?.startListening()
    }

    private fun createNotificationChannel() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val channel =
                NotificationChannel(
                    "voice_assistant",
                    "Voice Assistant",
                    NotificationManager
                        .IMPORTANCE_LOW
                )

            val manager =
                getSystemService(
                    NotificationManager::class.java
                )

            manager.createNotificationChannel(
                channel
            )
        }
    }

    private fun createNotification(): Notification {

        return if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            Notification.Builder(
                this,
                "voice_assistant"
            )
                .setContentTitle(
                    "Voice Assistant"
                )
                .setContentText(
                    "Floating microphone চালু আছে"
                )
                .setSmallIcon(
                    android.R.drawable
                        .ic_btn_speak_now
                )
                .build()

        } else {

            Notification.Builder(this)
                .setContentTitle(
                    "Voice Assistant"
                )
                .setContentText(
                    "Floating microphone চালু আছে"
                )
                .setSmallIcon(
                    android.R.drawable
                        .ic_btn_speak_now
                )
                .build()
        }
    }

    override fun onDestroy() {

        voiceRecognizer?.destroy()

        voiceRecognizer = null

        assistantController?.destroy()

        assistantController = null

        micButton?.let {

            try {
                windowManager.removeView(it)
            } catch (_: Exception) {
            }
        }

        micButton = null

        super.onDestroy()
    }

    override fun onBind(
        intent: Intent?
    ): IBinder? {
        return null
    }
}
