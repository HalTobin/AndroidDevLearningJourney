package com.chapeaumoineau.miavortoj.domain.use_case.favorite_language

import com.chapeaumoineau.miavortoj.domain.model.FavoriteLanguage
import com.chapeaumoineau.miavortoj.domain.repository.FavoriteLanguageRepository

class DeleteFavoriteLanguageUseCase(private val repository: FavoriteLanguageRepository) {

    suspend operator fun invoke(language: FavoriteLanguage) {
        repository.deleteFavoriteLanguage(language)
    }

}