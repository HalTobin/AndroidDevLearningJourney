package com.chapeaumoineau.miavortoj.domain.repository

import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.feature.dictionary.model.DictionaryWithWords
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    fun getDictionaries(): Flow<List<DictionaryWithWords>>

    suspend fun getDictionaryById(id: Int): Dictionary?

    suspend fun insertDictionary(dictionary: Dictionary)

    suspend fun deleteDictionary(dictionary: Dictionary)

}