package com.chapeaumoineau.miavortoj.domain.use_case.word

import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetWordUseCase(private val repository: WordRepository) {
    operator fun invoke(id: Int): Flow<Word>? {
        return repository.getWordById(id)
    }
}