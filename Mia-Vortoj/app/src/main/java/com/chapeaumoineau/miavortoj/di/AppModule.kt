package com.chapeaumoineau.miavortoj.di

import android.app.Application
import androidx.room.Room
import com.chapeaumoineau.miavortoj.data.data_source.VocabularyDatabase
import com.chapeaumoineau.miavortoj.data.repository.DictionaryRepositoryImpl
import com.chapeaumoineau.miavortoj.data.repository.FavoriteLanguageRepositoryImpl
import com.chapeaumoineau.miavortoj.data.repository.WordRepositoryImpl
import com.chapeaumoineau.miavortoj.domain.repository.DictionaryRepository
import com.chapeaumoineau.miavortoj.domain.repository.FavoriteLanguageRepository
import com.chapeaumoineau.miavortoj.domain.repository.WordRepository
import com.chapeaumoineau.miavortoj.domain.use_case.DictionaryUseCases
import com.chapeaumoineau.miavortoj.domain.use_case.FavoriteLanguageUseCases
import com.chapeaumoineau.miavortoj.domain.use_case.WordUseCases
import com.chapeaumoineau.miavortoj.domain.use_case.dictionary.AddDictionaryUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.dictionary.DeleteDictionaryUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.dictionary.GetDictionariesUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.dictionary.GetDictionaryUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.favorite_language.AddFavoriteLanguageUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.favorite_language.DeleteFavoriteLanguageUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.favorite_language.GetFavoriteLanguageUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.favorite_language.GetFavoriteLanguagesUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.word.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideVocabularyDatabase(app: Application): VocabularyDatabase {
        return Room.databaseBuilder(app,
            VocabularyDatabase::class.java,
            VocabularyDatabase.DATABASE_NAME)
            .addCallback(VocabularyDatabase.prepopulate)
            .addMigrations(VocabularyDatabase.MIGRATION_2_3)
            .addMigrations(VocabularyDatabase.MIGRATION_3_4)
            .addMigrations(VocabularyDatabase.MIGRATION_4_5)
            .addMigrations(VocabularyDatabase.MIGRATION_5_6)
            .addMigrations(VocabularyDatabase.MIGRATION_6_7)
            .build()
    }

    @Provides
    @Singleton
    fun provideDictionaryRepository(db: VocabularyDatabase): DictionaryRepository {
        return DictionaryRepositoryImpl(db.dictionaryDao)
    }

    @Provides
    @Singleton
    fun provideDictionaryUseCases(repository: DictionaryRepository): DictionaryUseCases {
        return DictionaryUseCases(getDictionaries = GetDictionariesUseCase(repository), deleteDictionary = DeleteDictionaryUseCase(repository), addDictionary = AddDictionaryUseCase(repository), getDictionary = GetDictionaryUseCase(repository))
    }

    @Provides
    @Singleton
    fun provideWordRepository(db: VocabularyDatabase): WordRepository {
        return WordRepositoryImpl(db.wordDao)
    }

    @Provides
    @Singleton
    fun provideWordUseCases(repository: WordRepository): WordUseCases {
        return WordUseCases(getWords = GetWordsUseCase(repository),
                            getWordsFromDictionary = GetWordsByDictionaryUseCase(repository),
                            getOldWordByDictionaryId = GetOldWordByDictionaryIdUseCase(repository),
                            deleteWordsFromDictionary = DeleteWordsFromDictionaryUseCase(repository),
                            deleteWord = DeleteWordUseCase(repository),
                            changeWordLastTimestamp = ChangeWordLastTimestampUseCase(repository),
                            changeWordNbPlayed = ChangeWordNbPlayedUseCase(repository),
                            changeWordNbSucceed = ChangeWordNbSucceedUseCase(repository),
                            addWord = AddWordUseCase(repository),
                            getWord = GetWordUseCase(repository))
    }

    @Provides
    @Singleton
    fun provideFavoriteLanguageRepository(db: VocabularyDatabase): FavoriteLanguageRepository {
        return FavoriteLanguageRepositoryImpl(db.favoriteLanguageDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteLanguageUseCases(repository: FavoriteLanguageRepository): FavoriteLanguageUseCases {
        return FavoriteLanguageUseCases(getFavoriteLanguages = GetFavoriteLanguagesUseCase(repository), getFavoriteLanguage = GetFavoriteLanguageUseCase(repository), deleteFavoriteLanguage = DeleteFavoriteLanguageUseCase(repository), addFavoriteLanguage = AddFavoriteLanguageUseCase(repository))
    }
}