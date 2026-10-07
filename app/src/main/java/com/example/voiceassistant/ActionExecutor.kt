package com.example.voiceassistant

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

class ActionExecutor(
    private val context: Context
) {

    private val parser = CommandParser()

    fun execute(command: VoiceCommand) {

        when (command.intent) {

            CommandIntent.NAVIGATE_BACK -> {
                performBack()
            }

            CommandIntent.NAVIGATE_HOME -> {
                performHome()
            }

            CommandIntent.OPEN_RECENTS -> {
                openRecents()
            }

            CommandIntent.NEXT_VIDEO -> {
                VoiceAccessibilityService.instance?.swipeUp()
                    ?: showAccessibilityMessage()
            }

            CommandIntent.PREVIOUS_VIDEO -> {
                VoiceAccessibilityService.instance?.swipeDown()
                    ?: showAccessibilityMessage()
            }

            CommandIntent.SCROLL_UP -> {
                VoiceAccessibilityService.instance?.swipeDown()
                    ?: showAccessibilityMessage()
            }

            CommandIntent.SCROLL_DOWN -> {
                VoiceAccessibilityService.instance?.swipeUp()
                    ?: showAccessibilityMessage()
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

    private fun performBack() {

        VoiceAccessibilityService.instance?.performGlobalAction(
            android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK
        ) ?: showAccessibilityMessage()
    }

    private fun performHome() {

        VoiceAccessibilityService.instance?.performGlobalAction(
            android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME
        ) ?: showAccessibilityMessage()
    }

    private fun openRecents() {

        VoiceAccessibilityService.instance?.performGlobalAction(
            android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_RECENTS
        ) ?: showAccessibilityMessage()
    }

    private fun openCamera() {

        val intent = Intent("android.media.action.IMAGE_CAPTURE")

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }

    private fun searchWeb(query: String) {

        val url = "https://www.google.com/search?q=" +
                Uri.encode(query)

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }

    private fun openApp(appName: String) {

        val packageName = when {

            appName.contains("youtube") ||
                    appName.contains("ইউটিউব") ->
                "com.google.android.youtube"

            appName.contains("tiktok") ||
                    appName.contains("টিকটক") ->
                "com.zhiliaoapp.musically"

            appName.contains("facebook") ||
                    appName.contains("ফেসবুক") ->
                "com.facebook.katana"

            appName.contains("instagram") ||
                    appName.contains("ইনস্টাগ্রাম") ->
                "com.instagram.android"

            appName.contains("whatsapp") ||
                    appName.contains("হোয়াটসঅ্যাপ") ||
                    appName.contains("হোয়াটসঅ্যাপ") ->
                "com.whatsapp"

            appName.contains("chrome") ||
                    appName.contains("ক্রোম") ->
                "com.android.chrome"

            else -> null
        }

        if (packageName == null) {
            Toast.makeText(
                context,
                "এই অ্যাপটি শনাক্ত করা যায়নি",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val launchIntent =
            context.packageManager
                .getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {

            launchIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            context.startActivity(launchIntent)

        } else {

            Toast.makeText(
                context,
                "অ্যাপটি ফোনে ইনস্টল করা নেই",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showAccessibilityMessage() {

        Toast.makeText(
            context,
            "আগে Accessibility Service চালু করুন",
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent(
            Settings.ACTION_ACCESSIBILITY_SETTINGS
        )

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(intent)
    }
}
