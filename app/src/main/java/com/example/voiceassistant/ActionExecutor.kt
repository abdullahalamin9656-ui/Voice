package com.example.voiceassistant

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class ActionExecutor(
    private val context: Context
) {

    fun execute(
        command: VoiceCommand
    ) {

        when (command.intent) {

            CommandIntent.NAVIGATE_BACK -> {

                VoiceAccessibilityService
                    .instance
                    ?.goBack()
                    ?: accessibilityRequired()
            }

            CommandIntent.NAVIGATE_HOME -> {

                VoiceAccessibilityService
                    .instance
                    ?.goHome()
                    ?: accessibilityRequired()
            }

            CommandIntent.OPEN_RECENTS -> {

                VoiceAccessibilityService
                    .instance
                    ?.openRecents()
                    ?: accessibilityRequired()
            }

            CommandIntent.NEXT_VIDEO -> {

                VoiceAccessibilityService
                    .instance
                    ?.swipeUp()
                    ?: accessibilityRequired()
            }

            CommandIntent.PREVIOUS_VIDEO -> {

                VoiceAccessibilityService
                    .instance
                    ?.swipeDown()
                    ?: accessibilityRequired()
            }

            CommandIntent.SCROLL_UP -> {

                VoiceAccessibilityService
                    .instance
                    ?.swipeDown()
                    ?: accessibilityRequired()
            }

            CommandIntent.SCROLL_DOWN -> {

                VoiceAccessibilityService
                    .instance
                    ?.swipeUp()
                    ?: accessibilityRequired()
            }

            CommandIntent.OPEN_CAMERA -> {
                openCamera()
            }

            CommandIntent.OPEN_APP -> {

                command.parameter?.let {
                    openApp(it)
                }
            }

            CommandIntent.SEARCH -> {

                command.parameter?.let {
                    searchWeb(it)
                }
            }

            CommandIntent.UNKNOWN -> {

                Toast.makeText(
                    context,
                    "কমান্ড বুঝতে পারিনি",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openCamera() {

        val intent = Intent(
            "android.media.action.IMAGE_CAPTURE"
        )

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        try {
            context.startActivity(intent)
        } catch (_: Exception) {

            Toast.makeText(
                context,
                "ক্যামেরা খোলা যাচ্ছে না",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun searchWeb(
        query: String
    ) {

        val url =
            "https://www.google.com/search?q=" +
                    Uri.encode(query)

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        try {
            context.startActivity(intent)
        } catch (_: Exception) {

            Toast.makeText(
                context,
                "Search খোলা যাচ্ছে না",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openApp(
        appName: String
    ) {

        val packageName =
            when (appName.lowercase()) {

                "youtube" ->
                    "com.google.android.youtube"

                "tiktok" ->
                    "com.zhiliaoapp.musically"

                "facebook" ->
                    "com.facebook.katana"

                "instagram" ->
                    "com.instagram.android"

                "whatsapp" ->
                    "com.whatsapp"

                "chrome" ->
                    "com.android.chrome"

                else -> null
            }

        if (packageName == null) {

            Toast.makeText(
                context,
                "অ্যাপটি শনাক্ত করা যায়নি",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val launchIntent =
            context.packageManager
                .getLaunchIntentForPackage(
                    packageName
                )

        if (launchIntent == null) {

            Toast.makeText(
                context,
                "$appName ফোনে ইনস্টল করা নেই",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        launchIntent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        try {
            context.startActivity(
                launchIntent
            )
        } catch (_: Exception) {

            Toast.makeText(
                context,
                "$appName খোলা যাচ্ছে না",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun accessibilityRequired() {

        Toast.makeText(
            context,
            "এই কাজের জন্য Accessibility Service চালু করুন",
            Toast.LENGTH_LONG
        ).show()
    }
}
