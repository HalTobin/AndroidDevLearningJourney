package com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word

sealed class AddEditWordEvent{
    data class EnteredSource(val value: String): AddEditWordEvent()
    data class EnteredTarget(val value: String): AddEditWordEvent()
    data class EnteredDescription(val value: String): AddEditWordEvent()
    data class EnteredEmote(val value: String): AddEditWordEvent()
    data class EnteredSearch(val value: String): AddEditWordEvent()
    data class OnCategorySelected(val category: Int): AddEditWordEvent()
    object DismissCategoryDialog: AddEditWordEvent()
    object SaveWord: AddEditWordEvent()
}
