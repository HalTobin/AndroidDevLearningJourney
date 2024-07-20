package com.example.budgetcalculator.domain.use_case.operation

import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.repository.OperationRepository

class GetOperationUseCase(private val repository: OperationRepository) {
    suspend operator fun invoke(id: Int) : Operation? {
        return repository.getOperation(id)
    }
}