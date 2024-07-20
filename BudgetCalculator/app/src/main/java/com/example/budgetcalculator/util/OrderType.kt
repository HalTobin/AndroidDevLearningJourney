package com.example.budgetcalculator.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}