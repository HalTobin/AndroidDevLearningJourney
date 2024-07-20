package com.chapeaumoineau.miavortoj.feature.words.presentation.words

import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.util.OrderType
import com.chapeaumoineau.miavortoj.feature.words.util.WordOrder

data class WordsState(val words: List<Word> = emptyList(),
                      val wordOrder: WordOrder = WordOrder.Source(OrderType.Ascending),
                      val isOrderSectionVisible: Boolean = false,
                      val isDeleteDialogVisible: Boolean = false,
                      val isWarningDialogVisible: Boolean = false,
                      val wordEdit: Int = 1,
                      val wordDelete: Word? = null)