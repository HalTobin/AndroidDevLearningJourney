package com.example.budgetcalculator.ui.main

import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.model.Summary
import com.example.budgetcalculator.domain.use_case.operation.OperationUseCases
import javax.inject.Inject

class MainModel @Inject constructor(
    private val operationUseCases: OperationUseCases
) : MainContract.Model {

    private var values = Summary()

    override suspend fun insertOperation(operation: Operation) = operationUseCases.insertOperationUseCase(operation)

    override fun getOperations() = operationUseCases.getOperationsUseCase()

    override suspend fun deleteOperation(operation: Operation) = operationUseCases.deleteOperationUseCase(operation)

    override fun setSummary(summary: Summary) { values = summary }

    override fun getValues(): Summary = values

}