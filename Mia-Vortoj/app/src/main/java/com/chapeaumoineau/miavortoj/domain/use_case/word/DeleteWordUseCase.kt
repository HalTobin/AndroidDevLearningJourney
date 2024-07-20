package com.chapeaumoineau.miavortoj.domain.use_case.word

import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.repository.WordRepository

class DeleteWordUseCase(private val repository: WordRepository) {

    suspend operator fun invoke(word: Word) {
        repository.deleteWord(word)
    }

}