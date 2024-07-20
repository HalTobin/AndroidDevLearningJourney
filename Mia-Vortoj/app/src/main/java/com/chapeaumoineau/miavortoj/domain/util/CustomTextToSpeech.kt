package com.chapeaumoineau.miavortoj.domain.util

import android.content.Context
import android.speech.tts.TextToSpeech
import com.chapeaumoineau.miavortoj.domain.model.Language

data class CustomTextToSpeech(
    var tts: TextToSpeech? = null,
    var speech: Boolean = false
) {

    fun initTextToSpeech(context: Context, language: Language) {
        if(language.locale != null) {
            tts = TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    if(tts!!.isLanguageAvailable(language.locale) == TextToSpeech.LANG_AVAILABLE) {
                        speech = true
                        tts!!.language = language.locale
                    }
                }
            }
        }
    }

    fun play(input: String) {
        if(speech) {
            tts!!.setPitch(1f)
            tts!!.speak(input, TextToSpeech.QUEUE_ADD, null, null)
        }
    }

}