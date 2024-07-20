package com.example.budgetcalculator.domain.model

import android.content.Context
import com.example.budgetcalculator.R

data class OperationType(
    var id: Int = TYPE_IN_UNDEFINED,
    val text: Int = R.string.type_in_undefined,
    val drawable: Int = R.drawable.type_undefined,
    val isIncome: Boolean = true
) {

    companion object {
        /** Types of incomes **/
        const val TYPE_IN_UNDEFINED = 0x100
        const val TYPE_SALARY = 0x101
        const val TYPE_ALLOWANCES_HELP = 0x102
        const val TYPE_POCKET_MONEY = 0x103
        const val TYPE_DIVIDENDS = 0x104

        /** Types of outcomes **/
        const val TYPE_OUT_UNDEFINED = 0x200
        const val TYPE_RENT = 0x201
        const val TYPE_CREDIT = 0x202
        const val TYPE_INTERNET_PHONE = 0x203
        const val TYPE_INVESTMENT = 0x204
        const val TYPE_SAVINGS = 0x205
        const val TYPE_DONATION = 0x206
        const val TYPE_SUBSCRIPTION = 0x207
        const val TYPE_TAXES = 0x208
        const val TYPE_HOUSE = 0x209
        const val TYPE_BANK = 0x20A
        const val TYPE_SHOPPING = 0x20B

        val undefinedTextResource = R.string.type_in_undefined

        private val types = listOf(
            OperationType(id = TYPE_IN_UNDEFINED, text = undefinedTextResource, drawable = R.drawable.type_undefined, isIncome = true),
            OperationType(id = TYPE_SALARY, text = R.string.type_in_salary, drawable = R.drawable.type_salary, isIncome = true),
            OperationType(id = TYPE_ALLOWANCES_HELP, text = R.string.type_in_allowances, drawable = R.drawable.type_allowances, isIncome = true),
            OperationType(id = TYPE_POCKET_MONEY, text = R.string.type_in_pocket, drawable = R.drawable.type_pocket_money, isIncome = true),
            OperationType(id = TYPE_DIVIDENDS, text = R.string.type_in_dividends, drawable = R.drawable.type_dividends, isIncome = true),

            OperationType(id = TYPE_OUT_UNDEFINED, text = undefinedTextResource, drawable = R.drawable.type_undefined, isIncome = false),
            OperationType(id = TYPE_RENT, text = R.string.type_out_rent, drawable = R.drawable.type_rent, isIncome = false),
            OperationType(id = TYPE_CREDIT, text = R.string.type_out_credit, drawable = R.drawable.type_credit, isIncome = false),
            OperationType(id = TYPE_INTERNET_PHONE, text = R.string.type_out_internet_phone, drawable = R.drawable.type_internet_phone, isIncome = false),
            OperationType(id = TYPE_INVESTMENT, text = R.string.type_out_investment, drawable = R.drawable.type_investment, isIncome = false),
            OperationType(id = TYPE_SAVINGS, text = R.string.type_out_savings, drawable = R.drawable.type_savings, isIncome = false),
            OperationType(id = TYPE_DONATION, text = R.string.type_out_donation, drawable = R.drawable.type_donation, isIncome = false),
            OperationType(id = TYPE_SUBSCRIPTION, text = R.string.type_out_subscription, drawable = R.drawable.type_subscription, isIncome = false),
            OperationType(id = TYPE_TAXES, text = R.string.type_out_taxes, drawable = R.drawable.type_taxes, isIncome = false),
            OperationType(id = TYPE_HOUSE, text = R.string.type_out_house, drawable = R.drawable.type_house, isIncome = false),
            OperationType(id = TYPE_BANK, text = R.string.type_out_bank, drawable = R.drawable.type_bank, isIncome = false),
            OperationType(id = TYPE_SHOPPING, text = R.string.type_out_shopping, drawable = R.drawable.type_shopping, isIncome = false),
        )

        fun getListByIsIncome(isIncome: Boolean) : List<OperationType> {
            val list = mutableListOf<OperationType>()

            types.forEach {
                if(it.isIncome == isIncome) list.add(it)
            }

            return list.toList()
        }

        fun findTextByTypeAndIsIncome(type: Int, isIncome: Boolean) : Int {
            var res = 0
            var listOfTypes = getListByIsIncome(isIncome)

            listOfTypes.forEach { operationType ->
                val toCompare = operationType.id
                if (type == toCompare) res = operationType.text
            }

            return res
        }

        fun findIdByTextAndIsIncome(context: Context, text: String, isIncome: Boolean) : Int {
            var id = 0

            getListByIsIncome(isIncome).forEach { operationType ->
                val toCompare = context.resources.getString(operationType.text)
                if (text == toCompare) id = operationType.id
            }

            return id
        }

        fun getDrawableByOperationId(id: Int) : Int {
            var drawable = R.drawable.type_undefined
            types.forEach { operationType ->
                if (operationType.id == id) drawable = operationType.drawable
            }
            return drawable
        }

    }

}