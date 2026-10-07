package com.example.voiceassistant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AssistantViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _state =
        MutableStateFlow(
            AssistantState()
        )

    val state: StateFlow<AssistantState> =
        _state

    private val controller =
        AssistantController(application)

    fun processVoice(text: String) {

        _state.value =
            _state.value.copy(
                isListening = false,
                lastHeardText = text,
                errorMessage = null
            )

        controller.processVoice(text)
    }

    fun setListening(value: Boolean) {

        _state.value =
            _state.value.copy(
                isListening = value,
                errorMessage = null
            )
    }

    fun setError(message: String) {

        _state.value =
            _state.value.copy(
                isListening = false,
                errorMessage = message
            )
    }

    override fun onCleared() {

        controller.destroy()

        super.onCleared()
    }
}
