package com.example.budgetcalculator.domain.use_case.operation

import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.repository.OperationRepository
import com.example.budgetcalculator.util.OperationOrder
import com.example.budgetcalculator.util.OrderType
import com.example.budgetcalculator.util.customFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetOperationsUseCase(private val repository: OperationRepository) {

    operator fun invoke(operationOrder: OperationOrder = OperationOrder.Type(OrderType.Ascending)): Flow<List<Operation>> {
        return repository.getOperations().map { operations ->
            when (operationOrder.orderType) {
                is OrderType.Ascending -> {
                    when (operationOrder) {
                        is OperationOrder.Title -> operations.sortedBy { it.title.customFormat() }
                        is OperationOrder.Type -> operations.sortedBy { it.type }
                        is OperationOrder.Amount -> operations.sortedBy { it.amount }
                        is OperationOrder.Income -> operations.sortedBy { it.isIncome }
                    }
                }
                is OrderType.Descending -> {
                    when (operationOrder) {
                        is OperationOrder.Title -> operations.sortedByDescending { it.title.customFormat() }
                        is OperationOrder.Type -> operations.sortedByDescending { it.type }
                        is OperationOrder.Amount -> operations.sortedByDescending { it.amount }
                        is OperationOrder.Income -> operations.sortedByDescending { it.isIncome }
                    }
                }
            }
        }
    }

}