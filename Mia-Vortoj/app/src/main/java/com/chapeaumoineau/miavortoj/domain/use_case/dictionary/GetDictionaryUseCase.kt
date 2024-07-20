package com.chapeaumoineau.miavortoj.domain.use_case.dictionary

import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.repository.DictionaryRepository

class GetDictionaryUseCase(private val repository: DictionaryRepository) {
    suspend operator fun invoke(id: Int): Dictionary? {
        return repository.getDictionaryById(id)
    }
}