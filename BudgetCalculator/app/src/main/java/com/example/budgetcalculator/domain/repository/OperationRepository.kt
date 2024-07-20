package com.example.budgetcalculator.domain.repository

import com.example.budgetcalculator.domain.model.Operation
import kotlinx.coroutines.flow.Flow

interface OperationRepository {

    fun getOperations() : Flow<List<Operation>>

    suspend fun getOperation(operationId: Int) : Operation?

    suspend fun insertOperation(operation: Operation) : Long

    suspend fun deleteOperation(operation: Operation)

}