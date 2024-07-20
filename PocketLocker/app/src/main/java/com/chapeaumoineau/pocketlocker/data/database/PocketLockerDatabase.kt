package com.chapeaumoineau.pocketlocker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chapeaumoineau.pocketlocker.data.database.dao.AlbumDao
import com.chapeaumoineau.pocketlocker.data.model.Album

@Database(
    entities = [Album::class],
    version = 1
)
abstract class PocketLockerDatabase: RoomDatabase() {

    abstract val albumDao: AlbumDao

    companion object {
        const val DATABASE_NAME = "private_album_db"
    }

}