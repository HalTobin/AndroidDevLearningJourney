package com.chapeaumoineau.miavortoj.domain.use_case.word

import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.repository.WordRepository

class GetOldWordByDictionaryIdUseCase(private val repository: WordRepository) {
    suspend operator fun invoke(dictionaryId: Int): Word? {
        return repository.getOldWordByDictionaryId(dictionaryId)
    }
}