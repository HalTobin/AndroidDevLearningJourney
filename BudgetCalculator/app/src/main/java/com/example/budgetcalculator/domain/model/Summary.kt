package com.example.budgetcalculator.domain.model

import java.text.DecimalFormat

data class Summary(
    var incomesRawAnnual: Float = 0f,
    var incomesRawMonthly: Float = 0f,
    var incomesAnnual: Float = 0f,
    var incomesMonthly: Float = 0f,
    var outcomesAnnual: Float = 0f,
    var outcomesMonthly: Float = 0f,
    var asideAnnual: Float = 0f,
    var asideMonthly: Float = 0f
) {

    fun setAnnualValues(incomes: Float, outcomes : Float, aside: Float) {
        incomesRawAnnual = incomes
        incomesRawMonthly = incomesRawAnnual / 12f
        incomesAnnual = incomes - outcomes
        incomesMonthly = incomesAnnual / 12f
        outcomesAnnual = outcomes
        outcomesMonthly = outcomesAnnual / 12f
        asideAnnual = aside
        asideMonthly = asideAnnual / 12f
    }

    fun getValue(value: Int, isYear: Boolean): String {
        val df = DecimalFormat("#,###,###.##")
        var amount = 0f

        when (value) {
            INCOMES_RAW -> amount = if (isYear) incomesRawAnnual else incomesRawMonthly
            INCOMES_NET -> amount = if (isYear) incomesAnnual else incomesMonthly
            OUTCOMES -> amount = if (isYear) outcomesAnnual else outcomesMonthly
            ASIDE -> amount = if (isYear) asideAnnual else asideMonthly
        }

        return df.format(amount)
    }

    companion object {
        const val INCOMES_RAW = 0
        const val INCOMES_NET = 1
        const val OUTCOMES = 2
        const val ASIDE = 3
    }

}