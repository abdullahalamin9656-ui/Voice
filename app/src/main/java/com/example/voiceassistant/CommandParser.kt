package com.example.voiceassistant

class CommandParser {

    fun parse(text: String): VoiceCommand {

        val original = text.trim()
        val input = original.lowercase()

        // Back
        if (
            input.contains("back") ||
            input.contains("পিছনে যাও") ||
            input.contains("আগের পেজ") ||
            input.contains("আগের পেইজ")
        ) {
            return VoiceCommand(
                original,
                CommandIntent.NAVIGATE_BACK
            )
        }

        // Home
        if (
            input == "home" ||
            input.contains("হোমে যাও") ||
            input.contains("হোমে নিয়ে যাও")
        ) {
            return VoiceCommand(
                original,
                CommandIntent.NAVIGATE_HOME
            )
        }

        // Recent apps
        if (
            input.contains("recent") ||
            input.contains("রিসেন্ট") ||
            input.contains("recent apps")
        ) {
            return VoiceCommand(
                original,
                CommandIntent.OPEN_RECENTS
            )
        }

        // Next video
        if (
            input.contains("next video") ||
            input.contains("পরের ভিডিও") ||
            input.contains("নেক্সট ভিডিও") ||
            input.contains("উপরের ভিডিও")
        ) {
            return VoiceCommand(
                original,
                CommandIntent.NEXT_VIDEO
            )
        }

        // Previous video
        if (
            input.contains("previous video") ||
            input.contains("আগের ভিডিও") ||
            input.contains("প্রিভিয়াস ভিডিও") ||
            input.contains("নিচের ভিডিও")
        ) {
            return VoiceCommand(
                original,
                CommandIntent.PREVIOUS_VIDEO
            )
        }

        // Scroll up
        if (
            input.contains("scroll up") ||
            input.contains("উপরে স্ক্রল") ||
            input.contains("উপরে যাও")
        ) {
            return VoiceCommand(
                original,
                CommandIntent.SCROLL_UP
            )
        }

        // Scroll down
        if (
            input.contains("scroll down") ||
            input.contains("নিচে স্ক্রল") ||
            input.contains("নিচে যাও")
        ) {
            return VoiceCommand(
                original,
                CommandIntent.SCROLL_DOWN
            )
        }

        // Camera
        if (
            input.contains("camera") ||
            input.contains("ক্যামেরা")
        ) {
            return VoiceCommand(
                original,
                CommandIntent.OPEN_CAMERA
            )
        }

        // Open app
        val appNames = listOf(
            "youtube",
            "tiktok",
            "facebook",
            "instagram",
            "whatsapp",
            "chrome",
            "youtube",
            "ইউটিউব",
            "টিকটক",
            "ফেসবুক",
            "ইনস্টাগ্রাম",
            "হোয়াটসঅ্যাপ",
            "হোয়াটসঅ্যাপ",
            "ক্রোম"
        )

        for (app in appNames) {

            if (
                input.contains("open $app") ||
                input.contains("অপেন $app") ||
                input.contains("খুল $app") ||
                input.contains("খোলো $app")
            ) {
                return VoiceCommand(
                    original,
                    CommandIntent.OPEN_APP,
                    app
                )
            }
        }

        // Search
        val searchPrefixes = listOf(
            "search",
            "google",
            "খুঁজে দেখ",
            "সার্চ",
            "খুঁজো"
        )

        for (prefix in searchPrefixes) {

            if (input.startsWith(prefix)) {

                val query = original
                    .substringAfter(prefix, "")
                    .trim()

                if (query.isNotEmpty()) {

                    return VoiceCommand(
                        original,
                        CommandIntent.SEARCH,
                        query
                    )
                }
            }
        }

        return VoiceCommand(
            original,
            CommandIntent.UNKNOWN
        )
    }
}
