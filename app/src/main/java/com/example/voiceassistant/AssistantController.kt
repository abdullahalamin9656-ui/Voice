package com.example.voiceassistant

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class AssistantController(
    private val context: Context
) {

    private val parser = CommandParser()

    private val executor =
        ActionExecutor(context)

    private var textToSpeech: TextToSpeech? = null

    init {

        textToSpeech =
            TextToSpeech(
                context
            ) { status ->

                if (status == TextToSpeech.SUCCESS) {

                    textToSpeech?.language =
                        Locale("bn", "BD")
                }
            }
    }

    fun processVoice(text: String) {

        val command =
            parser.parse(text)

        speak(
            when (command.intent) {

                CommandIntent.NAVIGATE_BACK ->
                    "পিছনে যাচ্ছি"

                CommandIntent.NAVIGATE_HOME ->
                    "হোমে যাচ্ছি"

                CommandIntent.OPEN_RECENTS ->
                    "রিসেন্ট অ্যাপ খুলছি"

                CommandIntent.NEXT_VIDEO ->
                    "পরের ভিডিও"

                CommandIntent.PREVIOUS_VIDEO ->
                    "আগের ভিডিও"

                CommandIntent.SCROLL_UP ->
                    "উপরে স্ক্রল করছি"

                CommandIntent.SCROLL_DOWN ->
                    "নিচে স্ক্রল করছি"

                CommandIntent.OPEN_CAMERA ->
                    "ক্যামেরা খুলছি"

                CommandIntent.OPEN_APP ->
                    "${command.parameter ?: "অ্যাপ"} খুলছি"

                CommandIntent.SEARCH ->
                    "${command.parameter ?: "বিষয়"} সার্চ করছি"

                CommandIntent.UNKNOWN ->
                    "দুঃখিত, কমান্ডটি বুঝতে পারিনি"
            }
        )

        executor.execute(command)
    }

    private fun speak(text: String) {

        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "assistant_response"
        )
    }

    fun destroy() {

        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
