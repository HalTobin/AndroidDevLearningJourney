package com.chapeaumoineau.miavortoj.feature.dictionary.model

import androidx.room.Embedded
import androidx.room.Relation
import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.model.Word

data class DictionaryWithWords(
    @Embedded
    val dictionary: Dictionary,
    @Relation(
        parentColumn = "id",
        entityColumn = "dictionaryId"
    )
    val words: List<Word>
)