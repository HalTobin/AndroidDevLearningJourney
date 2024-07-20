package com.example.budgetcalculator.domain.use_case.operation

import com.example.budgetcalculator.domain.model.InvalidOperationException
import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.repository.OperationRepository
import kotlin.jvm.Throws

class InsertOperationUseCase(private val repository: OperationRepository) {

    @Throws(InvalidOperationException::class)
    suspend operator fun invoke(operation: Operation) {
        if(operation.title.isBlank()) {
            throw InvalidOperationException("The title of the operation can't be empty")
        }
        repository.insertOperation(operation)
    }

}