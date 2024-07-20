package com.chapeaumoineau.miavortoj.feature.words.presentation.word_card

sealed class WordCardEvent {
    object EditWord: WordCardEvent()
    object PlayWord: WordCardEvent()
}
