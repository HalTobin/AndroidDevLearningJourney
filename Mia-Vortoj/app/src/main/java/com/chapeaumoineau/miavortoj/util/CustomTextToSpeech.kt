package com.chapeaumoineau.miavortoj.util

import android.content.Context
import android.speech.tts.TextToSpeech
import com.chapeaumoineau.miavortoj.domain.model.Language

class CustomTextToSpeech(listener: OnTextToSpeechReady) {

    private var speech = false
    private val mCallback: OnTextToSpeechReady = listener

    lateinit var tts: TextToSpeech

    fun initTextToSpeech(context: Context, language: Language) {
        if(language.locale != null) {
            tts = TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    if(tts.isLanguageAvailable(language.locale) == TextToSpeech.LANG_AVAILABLE) {
                        speech = true
                        mCallback.onTextToSpeechReady(speech)
                    }
                    tts.language = language.locale
                }
            }
        }
    }

    fun speak(input: String) {
        if(speech) {
            tts.setPitch(1f)
            tts.speak(input, TextToSpeech.QUEUE_ADD, null, null)
        }
    }

    interface OnTextToSpeechReady {
        fun onTextToSpeechReady(isTextToSpeechReady: Boolean)
    }
}