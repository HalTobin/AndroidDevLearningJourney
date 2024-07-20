package com.chapeaumoineau.miavortoj.domain.use_case.dictionary

import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.repository.DictionaryRepository
import com.chapeaumoineau.miavortoj.feature.dictionary.model.DictionaryWithWords
import com.chapeaumoineau.miavortoj.feature.dictionary.util.DictionaryOrder
import com.chapeaumoineau.miavortoj.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDictionariesUseCase(private val repository: DictionaryRepository) {

    operator fun invoke(dictionaryOrder: DictionaryOrder = DictionaryOrder.Words(OrderType.Ascending)): Flow<List<DictionaryWithWords>> {
        return repository.getDictionaries().map {
            dictionaries -> when(dictionaryOrder.orderType) {
                is OrderType.Ascending -> {
                    when(dictionaryOrder) {
                        is DictionaryOrder.Title -> dictionaries.sortedBy { it.dictionary.title.lowercase() }
                        is DictionaryOrder.Language -> dictionaries.sortedBy { it.dictionary.languageIso }
                        is DictionaryOrder.Words -> dictionaries.sortedByDescending { it.words.size }
                    }
                }
                is OrderType.Descending -> {
                    when(dictionaryOrder) {
                        is DictionaryOrder.Title -> dictionaries.sortedByDescending { it.dictionary.title.lowercase() }
                        is DictionaryOrder.Language -> dictionaries.sortedByDescending { it.dictionary.languageIso }
                        is DictionaryOrder.Words -> dictionaries.sortedBy { it.words.size }
                    }
                }
            }
        }
    }

}