package com.chapeaumoineau.miavortoj.feature.dictionary.util

import com.chapeaumoineau.miavortoj.util.OrderType

sealed class DictionaryOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): DictionaryOrder(orderType)
    class Language(orderType: OrderType): DictionaryOrder(orderType)
    class Words(orderType: OrderType): DictionaryOrder(orderType)

    fun copy(orderType: OrderType): DictionaryOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Language -> Language(orderType)
            is Words -> Words(orderType)
        }
    }
}