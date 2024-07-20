package com.chapeaumoineau.miavortoj.domain.use_case.dictionary

import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.repository.DictionaryRepository

class DeleteDictionaryUseCase(private val repository: DictionaryRepository) {

    suspend operator fun invoke(dictionary: Dictionary) {
        repository.deleteDictionary(dictionary)
    }

}