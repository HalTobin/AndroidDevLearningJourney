package com.chapeaumoineau.miavortoj.feature.dictionary.presentation.dictionnaries

import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.feature.dictionary.model.DictionaryWithWords
import com.chapeaumoineau.miavortoj.feature.dictionary.util.DictionaryOrder
import com.chapeaumoineau.miavortoj.util.OrderType

data class DictionariesState(
    val dictionaries: List<DictionaryWithWords> = emptyList(),
    val dictionaryOrder: DictionaryOrder = DictionaryOrder.Language(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val dictionaryEdit: Int = -1,
    val deleteConfirmationTextToEnter: String = "Delete",
    val dictionaryDelete: Dictionary? = null)