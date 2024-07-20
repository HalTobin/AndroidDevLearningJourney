package com.chapeaumoineau.miavortoj.data.data_source

import androidx.room.*
import com.chapeaumoineau.miavortoj.domain.model.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM word")
    fun getWords(): Flow<List<Word>>

    @Query("SELECT * FROM word WHERE dictionaryId = :dictionaryId")
    fun getWordsFromDictionary(dictionaryId: Int): Flow<List<Word>>

    @Query("SELECT * FROM word WHERE id = :id")
    fun getWordById(id: Int): Flow<Word>?

    @Query("SELECT * FROM word WHERE lastTestTimestamp = (SELECT MIN(lastTestTimestamp) FROM word WHERE dictionaryId = :dictionaryId)")
    suspend fun getOldWordByDictionaryId(dictionaryId: Int): Word?

    @Query("UPDATE word SET lastTestTimestamp = :timestamp WHERE id = :id")
    suspend fun changeWordLastTimestamp(id: Int, timestamp: Long)

    @Query("UPDATE word SET nbPlayed = :nbPlayed WHERE id = :id")
    suspend fun changeWordNbPlayed(id: Int, nbPlayed: Int)

    @Query("UPDATE word SET nbSucceed = :nbSucceed WHERE id = :id")
    suspend fun changeWordNbSucceed(id: Int, nbSucceed: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    @Query("DELETE FROM word WHERE dictionaryId = :dictionaryId")
    suspend fun deleteWordsFromDictionary(dictionaryId: Int)

    @Delete
    suspend fun deleteWord(word: Word)
}