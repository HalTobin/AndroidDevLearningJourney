package com.chapeaumoineau.miavortoj.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
