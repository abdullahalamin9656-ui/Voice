package com.example.voiceassistant

data class AssistantState(

    val isListening: Boolean = false,

    val lastHeardText: String = "",

    val lastResponse: String = "",

    val errorMessage: String? = null
)
