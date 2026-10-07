package com.example.voiceassistant

class CommandParser {

    fun parse(text: String): VoiceCommand {

        val original = text.trim()
        val input = normalize(original)

        if (input.isEmpty()) {
            return VoiceCommand(
                original,
                CommandIntent.UNKNOWN
            )
        }

        // =========================
        // BACK
        // =========================

        if (
            containsAny(
                input,
                "back",
                "go back",
                "পিছনে যাও",
                "পিছনে যাই",
                "পেছনে যাও",
                "আগের পেজে যাও",
                "আগের পেইজে যাও",
                "আগের পেজ",
                "আগের পেইজ",
                "back jao",
                "back যাও"
            )
        ) {
            return VoiceCommand(
                original,
                CommandIntent.NAVIGATE_BACK
            )
        }

        // =========================
        // HOME
        // =========================

        if (
            containsAny(
                input,
                "home",
                "go home",
                "হোমে যাও",
                "হোমে যাই",
                "হোমে নিয়ে যাও",
                "হোমে নিয়ে যাও",
                "home jao",
                "home যাও"
            )
        ) {
            return VoiceCommand(
                original,
                CommandIntent.NAVIGATE_HOME
            )
        }

        // =========================
        // RECENT APPS
        // =========================

        if (
            containsAny(
                input,
                "recent",
                "recent apps",
                "open recent",
                "রিসেন্ট",
                "রিসেন্ট অ্যাপ",
                "রিসেন্ট অ্যাপস",
                "recent app",
                "recent kholo",
                "recent খোলো"
            )
        ) {
            return VoiceCommand(
                original,
                CommandIntent.OPEN_RECENTS
            )
        }

        // =========================
        // NEXT VIDEO
        // =========================

        if (
            containsAny(
                input,
                "next video",
                "next reel",
                "next short",
                "পরের ভিডিও",
                "পরের রিল",
                "পরের শর্ট",
                "নেক্সট ভিডিও",
                "নেক্সট রিল",
                "next video jao",
                "next video যাও",
                "porer video",
                "porer reel"
            )
        ) {
            return VoiceCommand(
                original,
                CommandIntent.NEXT_VIDEO
            )
        }

        // =========================
        // PREVIOUS VIDEO
        // =========================

        if (
            containsAny(
                input,
                "previous video",
                "previous reel",
                "previous short",
                "আগের ভিডিও",
                "আগের রিল",
                "আগের শর্ট",
                "প্রিভিয়াস ভিডিও",
                "প্রিভিয়াস রিল",
                "ager video",
                "ager reel"
            )
        ) {
            return VoiceCommand(
                original,
                CommandIntent.PREVIOUS_VIDEO
            )
        }

        // =========================
        // SCROLL UP
        // =========================

        if (
            containsAny(
                input,
                "scroll up",
                "scroll top",
                "উপরে স্ক্রল",
                "উপরে স্ক্রোল",
                "উপরের দিকে স্ক্রল",
                "উপরে যাও",
                "স্ক্রল উপরে",
                "scroll up koro",
                "scroll up করো"
            )
        ) {
            return VoiceCommand(
                original,
                CommandIntent.SCROLL_UP
            )
        }

        // =========================
        // SCROLL DOWN
        // =========================

        if (
            containsAny(
                input,
                "scroll down",
                "নিচে স্ক্রল",
                "নিচে স্ক্রোল",
                "নিচের দিকে স্ক্রল",
                "নিচে যাও",
                "স্ক্রল নিচে",
                "scroll down koro",
                "scroll down করো"
            )
        ) {
            return VoiceCommand(
                original,
                CommandIntent.SCROLL_DOWN
            )
        }

        // =========================
        // CAMERA
        // =========================

        if (
            containsAny(
                input,
                "camera",
                "open camera",
                "ক্যামেরা",
                "ক্যামেরা খোলো",
                "ক্যামেরা খুল",
                "camera kholo",
                "camera khulo"
            )
        ) {
            return VoiceCommand(
                original,
                CommandIntent.OPEN_CAMERA
            )
        }

        // =========================
        // OPEN APP
        // =========================

        val app =
            detectApp(input)

        if (app != null) {

            return VoiceCommand(
                original,
                CommandIntent.OPEN_APP,
                app
            )
        }

        // =========================
        // SEARCH
        // =========================

        val searchQuery =
            extractSearchQuery(original)

        if (!searchQuery.isNullOrBlank()) {

            return VoiceCommand(
                original,
                CommandIntent.SEARCH,
                searchQuery
            )
        }

        return VoiceCommand(
            original,
            CommandIntent.UNKNOWN
        )
    }

    private fun detectApp(
        input: String
    ): String? {

        val apps =
            listOf(

                "youtube" to listOf(
                    "youtube",
                    "ইউটিউব",
                    "youtube kholo",
                    "youtube খোলো",
                    "youtube খুল",
                    "ইউটিউব খোলো",
                    "ইউটিউব খুল"
                ),

                "tiktok" to listOf(
                    "tiktok",
                    "টিকটক",
                    "tik tok",
                    "tiktok kholo",
                    "টিকটক খোলো",
                    "টিকটক খুল"
                ),

                "facebook" to listOf(
                    "facebook",
                    "ফেসবুক",
                    "facebook kholo",
                    "ফেসবুক খোলো",
                    "ফেসবুক খুল"
                ),

                "instagram" to listOf(
                    "instagram",
                    "ইনস্টাগ্রাম",
                    "ইন্সটাগ্রাম",
                    "instagram kholo",
                    "ইনস্টাগ্রাম খোলো"
                ),

                "whatsapp" to listOf(
                    "whatsapp",
                    "হোয়াটসঅ্যাপ",
                    "হোয়াটসঅ্যাপ",
                    "whatsapp kholo",
                    "whatsapp খোলো"
                ),

                "chrome" to listOf(
                    "chrome",
                    "ক্রোম",
                    "google chrome",
                    "chrome kholo",
                    "ক্রোম খোলো"
                )
            )

        for ((name, keywords) in apps) {

            for (keyword in keywords) {

                if (
                    input == keyword ||
                    input.contains(
                        keyword
                    )
                ) {
                    return name
                }
            }
        }

        return null
    }

    private fun extractSearchQuery(
        original: String
    ): String? {

        val lower =
            original.lowercase()

        val prefixes =
            listOf(
                "search for",
                "search",
                "google search",
                "google",
                "সার্চ করো",
                "সার্চ কর",
                "সার্চ",
                "খুঁজে দেখ",
                "খুঁজে দাও",
                "খুঁজো",
                "search koro",
                "search করো",
                "search kor",
                "google e search koro",
                "google এ সার্চ করো",
                "google-এ সার্চ করো",
                "গুগলে সার্চ করো"
            )

        for (prefix in prefixes) {

            if (
                lower.startsWith(
                    prefix
                )
            ) {

                val query =
                    original
                        .substring(
                            prefix.length
                        )
                        .trim()

                if (query.isNotBlank()) {
                    return cleanSearchQuery(
                        query
                    )
                }
            }
        }

        return null
    }

    private fun cleanSearchQuery(
        query: String
    ): String {

        return query
            .removePrefix("করো")
            .removePrefix("কর")
            .removePrefix("koro")
            .removePrefix("kor")
            .trim()
    }

    private fun containsAny(
        input: String,
        vararg values: String
    ): Boolean {

        for (value in values) {

            if (
                input.contains(
                    value.lowercase()
                )
            ) {
                return true
            }
        }

        return false
    }

    private fun normalize(
        text: String
    ): String {

        return text
            .lowercase()
            .replace(
                Regex("\\s+"),
                " "
            )
            .trim()
    }
}
