package com.chapeaumoineau.miavortoj.domain.repository

import com.chapeaumoineau.miavortoj.domain.model.FavoriteLanguage
import kotlinx.coroutines.flow.Flow

interface FavoriteLanguageRepository {

    fun getFavoriteLanguages(): Flow<List<FavoriteLanguage>>

    suspend fun getFavoriteLanguageById(id: Int): FavoriteLanguage?

    suspend fun insertFavoriteLanguage(favoriteLanguage: FavoriteLanguage)

    suspend fun deleteFavoriteLanguage(favoriteLanguage: FavoriteLanguage)

}