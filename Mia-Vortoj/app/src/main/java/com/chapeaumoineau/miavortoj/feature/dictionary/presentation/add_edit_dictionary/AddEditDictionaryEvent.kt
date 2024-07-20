package com.chapeaumoineau.miavortoj.feature.dictionary.presentation.add_edit_dictionary

sealed class AddEditDictionaryEvent{
    data class EnteredTitle(val value: String): AddEditDictionaryEvent()
    data class EnteredDescription(val value: String): AddEditDictionaryEvent()
    data class EnteredSearch(val value: String): AddEditDictionaryEvent()
    data class ChangeLanguage(val language: String): AddEditDictionaryEvent()
    object MoreLanguage: AddEditDictionaryEvent()
    data class OnNewLanguageSelected(val language: String): AddEditDictionaryEvent()
    object DismissLanguageDialog: AddEditDictionaryEvent()
    object SaveDictionary: AddEditDictionaryEvent()
}
