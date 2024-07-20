package com.example.budgetcalculator.domain.use_case.operation

import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.repository.OperationRepository

class DeleteOperationUseCase(private val repository: OperationRepository) {
    suspend operator fun invoke(operation: Operation) {
        repository.deleteOperation(operation)
    }
}