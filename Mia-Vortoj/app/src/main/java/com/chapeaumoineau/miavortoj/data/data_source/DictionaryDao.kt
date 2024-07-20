package com.chapeaumoineau.miavortoj.data.data_source

import androidx.room.*
import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.feature.dictionary.model.DictionaryWithWords
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {

    @Query("SELECT *,(SELECT COUNT(*) FROM Word WHERE Word.dictionaryId = Dictionary.id) AS nbWords FROM dictionary")
    fun getDictionaries(): Flow<List<DictionaryWithWords>>

    @Query("SELECT * FROM dictionary WHERE id = :id")
    suspend fun getDictionaryById(id: Int): Dictionary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDictionary(dictionary: Dictionary)

    @Delete
    suspend fun deleteDictionary(dictionary: Dictionary)
}