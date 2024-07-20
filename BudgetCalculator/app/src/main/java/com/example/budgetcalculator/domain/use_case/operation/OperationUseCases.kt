package com.example.budgetcalculator.domain.use_case.operation

data class OperationUseCases(
    val getOperationsUseCase: GetOperationsUseCase,
    val getOperationUseCase: GetOperationUseCase,
    val insertOperationUseCase: InsertOperationUseCase,
    val deleteOperationUseCase: DeleteOperationUseCase
)