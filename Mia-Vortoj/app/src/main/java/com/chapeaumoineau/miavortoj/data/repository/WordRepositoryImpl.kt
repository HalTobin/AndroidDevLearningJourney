package com.chapeaumoineau.miavortoj.data.repository

import com.chapeaumoineau.miavortoj.data.data_source.WordDao
import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class WordRepositoryImpl(private val dao: WordDao) : WordRepository {
    override fun getWords(): Flow<List<Word>> {
        return dao.getWords()
    }

    override fun getWordsFromDictionary(dictionaryId: Int): Flow<List<Word>> {
        return dao.getWordsFromDictionary(dictionaryId)
    }

    override fun getWordById(id: Int): Flow<Word>? {
        return dao.getWordById(id)
    }

    override suspend fun getOldWordByDictionaryId(dictionaryId: Int): Word? {
        return dao.getOldWordByDictionaryId(dictionaryId)
    }

    override suspend fun changeWordLastTimestamp(id: Int, timestamp: Long) {
        dao.changeWordLastTimestamp(id, timestamp)
    }

    override suspend fun changeWordNbSucceed(id: Int, nbSucceed: Int) {
        dao.changeWordNbSucceed(id, nbSucceed)
    }

    override suspend fun changeWordNbPlayed(id: Int, nbPlayed: Int) {
        dao.changeWordNbPlayed(id, nbPlayed)
    }

    override suspend fun insertWord(word: Word) {
        dao.insertWord(word)
    }

    override suspend fun deleteWordsFromDictionary(dictionaryId: Int) {
        dao.deleteWordsFromDictionary(dictionaryId)
    }

    override suspend fun deleteWord(word: Word) {
        dao.deleteWord(word)
    }
}