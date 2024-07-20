package com.chapeaumoineau.pocketlocker.di

import android.app.Application
import androidx.room.Room
import com.chapeaumoineau.pocketlocker.data.database.PocketLockerDatabase
import com.chapeaumoineau.pocketlocker.data.database.dao.AlbumDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePocketLockerDatabase(app: Application): PocketLockerDatabase =
        Room.databaseBuilder(app, PocketLockerDatabase::class.java, PocketLockerDatabase.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideAlbumDao(db: PocketLockerDatabase): AlbumDao = db.albumDao

}