package com.chapeaumoineau.miavortoj.data.repository

import com.chapeaumoineau.miavortoj.data.data_source.DictionaryDao
import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.repository.DictionaryRepository
import com.chapeaumoineau.miavortoj.feature.dictionary.model.DictionaryWithWords
import kotlinx.coroutines.flow.Flow

class DictionaryRepositoryImpl(private val dao: DictionaryDao) : DictionaryRepository {
    override fun getDictionaries(): Flow<List<DictionaryWithWords>> {
        return dao.getDictionaries()
    }

    override suspend fun getDictionaryById(id: Int): Dictionary? {
        return dao.getDictionaryById(id)
    }

    override suspend fun insertDictionary(dictionary: Dictionary) {
        dao.insertDictionary(dictionary)
    }

    override suspend fun deleteDictionary(dictionary: Dictionary) {
        dao.deleteDictionary(dictionary)
    }
}