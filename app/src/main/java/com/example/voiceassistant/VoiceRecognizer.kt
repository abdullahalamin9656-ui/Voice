package com.example.voiceassistant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class VoiceRecognizer(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit
) {

    private var speechRecognizer: SpeechRecognizer? = null

    fun startListening() {

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("এই ফোনে Speech Recognition available নেই")
            return
        }

        speechRecognizer?.destroy()

        speechRecognizer =
            SpeechRecognizer.createSpeechRecognizer(context)

        speechRecognizer?.setRecognitionListener(
            object : RecognitionListener {

                override fun onReadyForSpeech(params: Bundle?) {}

                override fun onBeginningOfSpeech() {}

                override fun onRmsChanged(rmsdB: Float) {}

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {}

                override fun onPartialResults(
                    partialResults: Bundle?
                ) {
                }

                override fun onEvent(
                    eventType: Int,
                    params: Bundle?
                ) {
                }

                override fun onResults(
                    results: Bundle?
                ) {

                    val matches =
                        results?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )

                    val text = matches
                        ?.firstOrNull()
                        ?.trim()

                    if (!text.isNullOrEmpty()) {
                        onResult(text)
                    } else {
                        onError("কিছু শোনা যায়নি")
                    }
                }

                override fun onError(error: Int) {

                    val message = when (error) {

                        SpeechRecognizer.ERROR_AUDIO ->
                            "মাইক্রোফোনে সমস্যা হয়েছে"

                        SpeechRecognizer.ERROR_CLIENT ->
                            "Speech Recognizer error"

                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                            "Microphone permission প্রয়োজন"

                        SpeechRecognizer.ERROR_NETWORK ->
                            "ইন্টারনেট সংযোগে সমস্যা"

                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                            "Network timeout"

                        SpeechRecognizer.ERROR_NO_MATCH ->
                            "কথা বুঝতে পারিনি"

                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                            "Speech Recognizer ব্যস্ত"

                        SpeechRecognizer.ERROR_SERVER ->
                            "Speech server error"

                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                            "কথা শোনা যায়নি"

                        else ->
                            "Voice recognition error"
                    }

                    onError(message)
                }
            }
        )

        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "bn-BD"
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            "bn-BD"
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_PARTIAL_RESULTS,
            true
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_MAX_RESULTS,
            5
        )

        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {

        speechRecognizer?.stopListening()
    }

    fun destroy() {

        speechRecognizer?.destroy()

        speechRecognizer = null
    }
}
