package com.chapeaumoineau.miavortoj.feature.words.presentation.words

import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.feature.words.util.WordOrder

sealed class WordsEvent {
    data class Order(val wordOrder: WordOrder): WordsEvent()
    data class EnteredSearch(val value: String): WordsEvent()
    data class DeleteWord(val word: Word): WordsEvent()
    object ToggleOrderSection: WordsEvent()
    data class ToggleEditMode(val wordId: Int? = -1): WordsEvent()
    data class ToggleDeleteDialog(var word: Word): WordsEvent()
    object ToggleWarningDialog: WordsEvent()
    object DismissDeleteDialog: WordsEvent()
    object DismissWarningDialog: WordsEvent()

    object DismissRulesDialog: WordsEvent()
}
