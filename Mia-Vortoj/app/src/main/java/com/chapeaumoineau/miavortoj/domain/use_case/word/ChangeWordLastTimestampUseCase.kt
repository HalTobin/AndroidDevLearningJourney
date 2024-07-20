package com.chapeaumoineau.miavortoj.domain.use_case.word

import com.chapeaumoineau.miavortoj.domain.repository.WordRepository

class ChangeWordLastTimestampUseCase(private val repository: WordRepository) {
    suspend operator fun invoke(id: Int, timestamp: Long) {
        repository.changeWordLastTimestamp(id, timestamp)
    }
}