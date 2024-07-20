package com.example.budgetcalculator.util

sealed class OperationOrder(val orderType : OrderType) {
    class Title(orderType: OrderType) : OperationOrder(orderType)
    class Type(orderType: OrderType) : OperationOrder(orderType)
    class Amount(orderType: OrderType) : OperationOrder(orderType)
    class Income(orderType: OrderType) : OperationOrder(orderType)

    fun copy(orderType : OrderType) : OperationOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Type -> Type(orderType)
            is Amount -> Amount(orderType)
            is Income -> Income(orderType)
        }
    }

}
