package com.chapeaumoineau.miavortoj.domain.use_case.dictionary

import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.model.InvalidDictionaryException
import com.chapeaumoineau.miavortoj.domain.repository.DictionaryRepository

class AddDictionaryUseCase(private val repository: DictionaryRepository) {

    @Throws(InvalidDictionaryException::class)
    suspend operator fun invoke(dictionary: Dictionary) {
        if(dictionary.title.isBlank()) {
            throw InvalidDictionaryException("The title of the dictionary can't be empty.")
        }
        if(dictionary.languageIso.isBlank()) {
            throw InvalidDictionaryException("The language of the dictionary can't be undetermined.")
        }
        repository.insertDictionary(dictionary)
    }
}