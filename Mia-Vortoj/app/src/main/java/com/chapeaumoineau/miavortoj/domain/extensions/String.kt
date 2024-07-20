package com.chapeaumoineau.miavortoj.domain.extensions

import java.text.Normalizer
import kotlin.String

fun String.lastChar(): Char {
    return this[this.length-1]
}

fun String.isLastCharASpace(): Boolean {
    return (this.lastChar() == ' ')
}

fun String.removeLastSpace(): String {
    return if (this.isNotEmpty()) if(this.isLastCharASpace()) this.dropLast(1) else this
    else this
}

fun String.formatCustom(): String {
    val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    var temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    temp = REGEX_UNACCENT.replace(temp, "").lowercase().removeLastSpace()
    return temp
}

fun String.equalsCustom(inStr: String): Boolean {
    return this.formatCustom() == inStr.formatCustom()
}

fun String.containsCustom(inStr: String): Boolean {
    return this.formatCustom().contains(inStr.formatCustom())
}

/**
 * Returns a minimal set of characters that have to be removed from (or added to) the respective
 * strings to make the strings equal.
 */
fun String.difference(a: String): Int {
    val diff = diffHelper(a.formatCustom(), this.formatCustom(), HashMap())
    return diff.first.length
}

/**
 * Recursively compute a minimal set of characters while remembering already computed substrings.
 * Runs in O(n^2).
 */
private fun diffHelper(
    a: String,
    b: String,
    lookup: MutableMap<Long, Pair<String>>
): Pair<String> {
    val key = a.length.toLong() shl 32 or b.length.toLong()
    if (!lookup.containsKey(key)) {
        val value: Pair<String> = if (a.isEmpty() || b.isEmpty()) {
            Pair(a, b)
        } else if (a[0] == b[0]) {
            diffHelper(a.substring(1), b.substring(1), lookup)
        } else {
            val aa = diffHelper(a.substring(1), b, lookup)
            val bb = diffHelper(a, b.substring(1), lookup)
            if (aa.first.length + aa.second.length < bb.first.length + bb.second.length) {
                Pair(a[0] + aa.first, aa.second)
            } else {
                Pair(bb.first, b[0] + bb.second)
            }
        }
        lookup[key] = value
    }
    return lookup[key]!!
}

class Pair<T>(val first: T, val second: T) {
    override fun toString(): String {
        return "($first,$second)"
    }
}