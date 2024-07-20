package com.chapeaumoineau.miavortoj.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Dictionary (
    val title: String,
    val description: String,
    val languageIso: String,
    @PrimaryKey val id: Int? = null
) {

    @Ignore var nbWords: Int = 0

    companion object {
        fun getDefault(): Dictionary {
            return Dictionary(title = "Title", description = "Description...", languageIso = "0_N/A")
        }
    }
}

class InvalidDictionaryException(message: String): Exception(message)