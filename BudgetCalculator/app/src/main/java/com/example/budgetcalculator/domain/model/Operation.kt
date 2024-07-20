package com.example.budgetcalculator.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Model for operations, can be an income as well as an outcome **/

@Entity
data class Operation(
    @PrimaryKey val id: Int? = null,
    var type: Int = OperationType.TYPE_IN_UNDEFINED,
    var title: String = "",
    var amount: Float = 0f,
    var isIncome: Boolean = true,
    var isAnnual: Boolean = true,
    var isOn: Boolean = true
) {

    fun invertOnOff() {
        isOn = !isOn
    }

}

class InvalidOperationException(message: String) : Exception(message)
