package com.example.budgetcalculator.data.repository

import com.example.budgetcalculator.data.data_source.OperationDao
import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.repository.OperationRepository
import kotlinx.coroutines.flow.Flow

class OperationRepositoryImpl(private val dao: OperationDao) : OperationRepository {
    override fun getOperations(): Flow<List<Operation>> {
        return dao.getOperations()
    }

    override suspend fun getOperation(operationId: Int): Operation? {
        return dao.getOperationById(operationId)
    }

    override suspend fun insertOperation(operation: Operation): Long {
        return dao.addOperation(operation)
    }

    override suspend fun deleteOperation(operation: Operation) {
        dao.deleteOperation(operation)
    }
}