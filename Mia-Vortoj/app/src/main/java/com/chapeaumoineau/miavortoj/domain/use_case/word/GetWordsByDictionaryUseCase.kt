package com.chapeaumoineau.miavortoj.domain.use_case.word

import com.chapeaumoineau.miavortoj.domain.extensions.formatCustom
import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.repository.WordRepository
import com.chapeaumoineau.miavortoj.util.OrderType
import com.chapeaumoineau.miavortoj.feature.words.util.WordOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWordsByDictionaryUseCase(private val repository: WordRepository) {
    operator fun invoke(dictionaryId: Int, wordOrder: WordOrder = WordOrder.Target(OrderType.Ascending)): Flow<List<Word>> {
        return repository.getWordsFromDictionary(dictionaryId).map {
                words -> when(wordOrder.orderType) {
            is OrderType.Ascending -> {
                when(wordOrder) {
                    is WordOrder.Target -> words.sortedBy { it.targetWord.formatCustom() }
                    is WordOrder.Source -> words.sortedBy { it.sourceWord.formatCustom() }
                    is WordOrder.Theme -> words.sortedBy { it.themeId }
                }
            }
            is OrderType.Descending -> {
                when(wordOrder) {
                    is WordOrder.Target -> words.sortedByDescending { it.targetWord.formatCustom() }
                    is WordOrder.Source -> words.sortedByDescending { it.sourceWord.formatCustom() }
                    is WordOrder.Theme -> words.sortedByDescending { it.themeId }
                }
            }
            }
        }
    }
}