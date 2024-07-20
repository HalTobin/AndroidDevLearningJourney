package com.example.budgetcalculator.util

import java.text.Normalizer

fun String.customFormat(): String {
    val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    var temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    temp = REGEX_UNACCENT.replace(temp, "").lowercase()
    return temp
}