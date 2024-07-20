package com.example.budgetcalculator.di

import android.app.Application
import androidx.room.Room
import com.example.budgetcalculator.data.BudgetDatabase
import com.example.budgetcalculator.data.repository.OperationRepositoryImpl
import com.example.budgetcalculator.domain.repository.OperationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideBudgetDatabase(app: Application) : BudgetDatabase {
        return Room.databaseBuilder(app,
            BudgetDatabase::class.java,
            BudgetDatabase.DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideOperationRepository(db: BudgetDatabase) : OperationRepository {
        return OperationRepositoryImpl(db.operationDao)
    }

}