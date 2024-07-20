package com.example.budgetcalculator.ui.main

import androidx.lifecycle.LiveData
import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.model.OperationType
import com.example.budgetcalculator.domain.model.Summary
import kotlinx.coroutines.flow.Flow

interface MainContract {

    interface View {
        fun displayOperation(operations: List<Operation>)

        fun displayValues(values: Summary)
    }

    interface Model {
        suspend fun insertOperation(operation: Operation)

        fun getOperations() : Flow<List<Operation>>

        suspend fun deleteOperation(operation: Operation)

        fun setSummary(summary: Summary)

        fun getValues() : Summary
    }

    interface Presenter {
        fun observeOperations() : LiveData<List<Operation>>

        fun onChangeIncomeOrOutcome(isIncome: Boolean) : List<OperationType>

        fun onAddOperationButtonClick(
            id: Int?,
            type: Int,
            title: String,
            amount: Float,
            isIncome: Boolean,
            isAnnual: Boolean
        )

        fun onDeleteOperationButtonClick(operation: Operation)

        fun onOnOffOperationClick(operation: Operation)

        fun refreshSummary()
    }

}