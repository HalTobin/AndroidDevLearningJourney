package com.chapeaumoineau.miavortoj.feature.dictionary.presentation.dictionnaries

import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.feature.dictionary.util.DictionaryOrder

sealed class DictionariesEvent {
    data class Order(val dictionaryOrder: DictionaryOrder): DictionariesEvent()
    data class DeleteDictionary(val dictionary: Dictionary): DictionariesEvent()
    object ToggleOrderSection: DictionariesEvent()
    data class ToggleEditMode(val dictionaryId: Int? = -1): DictionariesEvent()
    data class ToggleDeleteDialog(val dictionary: Dictionary, val deleteConfirmationToEnter: String): DictionariesEvent()
    data class EnteredDeleteValidation(val deleteValidation: String): DictionariesEvent()
    object DismissDeleteDialog: DictionariesEvent()
}
