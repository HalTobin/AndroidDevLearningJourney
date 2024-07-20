package com.chapeaumoineau.miavortoj.domain.use_case.word

import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.repository.WordRepository
import com.chapeaumoineau.miavortoj.util.OrderType
import com.chapeaumoineau.miavortoj.feature.words.util.WordOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWordsUseCase(private val repository: WordRepository) {

    operator fun invoke(wordOrder: WordOrder = WordOrder.Source(OrderType.Descending)): Flow<List<Word>> {
        return repository.getWords().map {
            words -> when(wordOrder.orderType) {
                is OrderType.Ascending -> {
                    when(wordOrder) {
                        is WordOrder.Target -> words.sortedBy { it.targetWord.lowercase() }
                        is WordOrder.Source -> words.sortedBy { it.sourceWord.lowercase() }
                        is WordOrder.Theme -> words.sortedBy { it.themeId }
                    }
                }
                is OrderType.Descending -> {
                    when(wordOrder) {
                        is WordOrder.Target -> words.sortedByDescending { it.targetWord.lowercase() }
                        is WordOrder.Source -> words.sortedByDescending { it.sourceWord.lowercase() }
                        is WordOrder.Theme -> words.sortedByDescending { it.themeId }
                    }
                }
            }
        }
    }

}