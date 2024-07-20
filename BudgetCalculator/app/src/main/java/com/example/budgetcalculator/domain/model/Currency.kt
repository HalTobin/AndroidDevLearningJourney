package com.example.budgetcalculator.domain.model

data class Currency(
    val id: Int = 0,
    val title: String = "",
    val icon: String = ""
) {

    companion object {

        val currencies = listOf(
            Currency(id = 1, title = "Dollar", icon = "$"),
            Currency(id = 2, title = "Euro", icon = "€"),
            Currency(id = 3, title = "Рубль", icon = "₽"),
            Currency(id = 4, title = "円", icon = "¥"),
        )

    }

}