package com.chapeaumoineau.pocketlocker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chapeaumoineau.pocketlocker.data.model.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album")
    fun getAllAlbums(): Flow<List<Album>>

    @Query("SELECT * FROM album WHERE id = :id")
    fun getAlbumById(id: Int): Album

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAlbum(album: Album)

}