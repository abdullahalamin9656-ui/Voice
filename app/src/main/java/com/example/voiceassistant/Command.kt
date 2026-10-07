package com.example.voiceassistant

enum class CommandIntent {

    OPEN_APP,

    SEARCH,

    NAVIGATE_BACK,

    NAVIGATE_HOME,

    OPEN_RECENTS,

    NEXT_VIDEO,

    PREVIOUS_VIDEO,

    SCROLL_UP,

    SCROLL_DOWN,

    OPEN_CAMERA,

    UNKNOWN
}

data class VoiceCommand(

    val rawText: String,

    val intent: CommandIntent,

    val parameter: String? = null

)
