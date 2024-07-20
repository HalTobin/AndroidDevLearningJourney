package com.example.budgetcalculator.di

import com.example.budgetcalculator.domain.repository.OperationRepository
import com.example.budgetcalculator.domain.use_case.operation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideOperationUseCases(repository: OperationRepository) : OperationUseCases {
        return OperationUseCases(
            getOperationsUseCase = GetOperationsUseCase(repository),
            getOperationUseCase = GetOperationUseCase(repository),
            insertOperationUseCase = InsertOperationUseCase(repository),
            deleteOperationUseCase = DeleteOperationUseCase(repository)
        )
    }

}