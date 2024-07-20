package com.chapeaumoineau.miavortoj.model

import com.chapeaumoineau.miavortoj.domain.model.Language
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LanguageUnitTest {

    @Test
    fun `how many languages`() {
        // Given
        val nbLanguages = Language.languagesList.size - 1

        // When
        println("Number of languages : ".plus(nbLanguages))

        // Assert
        assertThat(nbLanguages).isEqualTo(nbLanguages)
    }

    @Test
    fun `is there duplicates languages by iso`() {
        // Given
        val languagesIso = Language.languagesList.map { it.iso }

        // When
        val hasDuplicate = languagesIso.hasDuplicates()

        // Assert
        assertThat(hasDuplicate).isFalse()
    }

    @Test
    fun `is there duplicates languages by name`() {
        // Given
        val languagesName = Language.languagesList.map { it.name }

        // When
        val hasDuplicate = languagesName.hasDuplicates()

        // Assert
        assertThat(hasDuplicate).isFalse()
    }

    private fun List<String>.hasDuplicates(): Boolean {
        val result = hasDuplicates(this.toTypedArray())

        if (result){
            val duplicates = findAllDuplicates(this.toTypedArray()).toString()
            println("Found a duplicate : ".plus(duplicates))
        }

        return result;
    }

    private fun <T> hasDuplicates(arr: Array<T>): Boolean {
        return arr.size != arr.distinct().count();
    }

    private fun findAllDuplicates(array: Array<String>): Set<String> {
        val seen: MutableSet<String> = mutableSetOf()
        val duplicates: MutableSet<String> = mutableSetOf()
        for (i in array) {
            if (!seen.add(i)) {
                duplicates.add(i)
            }
        }
        return duplicates
    }

}