package com.chapeaumoineau.miavortoj.domain.use_case.word

import com.chapeaumoineau.miavortoj.domain.model.InvalidDictionaryException
import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.repository.WordRepository

class AddWordUseCase(private val repository: WordRepository) {

    @Throws(InvalidDictionaryException::class)
    suspend operator fun invoke(word: Word) {
        if(word.sourceWord.isBlank()) {
            throw InvalidDictionaryException("The source word field can't be empty.")
        }
        if(word.targetWord.isBlank()) {
            throw InvalidDictionaryException("The target word field can't be empty.")
        }
        repository.insertWord(word)
    }
}