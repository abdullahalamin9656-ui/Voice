package com.example.voiceassistant

import android.app.Application

class VoiceAssistantApp : Application() {

    companion object {

        lateinit var instance: VoiceAssistantApp
            private set
    }

    override fun onCreate() {

        super.onCreate()

        instance = this
    }
}
