package com.chapeaumoineau.miavortoj.domain.use_case.word

import com.chapeaumoineau.miavortoj.domain.repository.WordRepository

class ChangeWordNbPlayedUseCase(private val repository: WordRepository) {
    suspend operator fun invoke(id: Int, nbPlayed: Int) {
        repository.changeWordNbPlayed(id, nbPlayed)
    }
}