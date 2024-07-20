package com.chapeaumoineau.miavortoj.domain.use_case.favorite_language

import com.chapeaumoineau.miavortoj.domain.model.FavoriteLanguage
import com.chapeaumoineau.miavortoj.domain.repository.FavoriteLanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoriteLanguagesUseCase(private val repository: FavoriteLanguageRepository) {

    operator fun invoke(): Flow<List<FavoriteLanguage>> {
        return repository.getFavoriteLanguages().map { favoriteLanguages ->
            favoriteLanguages.sortedBy { it.timestamp }
        }
    }

}