package com.chapeaumoineau.miavortoj.domain.use_case

import com.chapeaumoineau.miavortoj.domain.use_case.favorite_language.AddFavoriteLanguageUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.favorite_language.DeleteFavoriteLanguageUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.favorite_language.GetFavoriteLanguageUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.favorite_language.GetFavoriteLanguagesUseCase

data class FavoriteLanguageUseCases(val getFavoriteLanguages: GetFavoriteLanguagesUseCase,
                                    val deleteFavoriteLanguage: DeleteFavoriteLanguageUseCase,
                                    val addFavoriteLanguage: AddFavoriteLanguageUseCase,
                                    val getFavoriteLanguage: GetFavoriteLanguageUseCase
)
