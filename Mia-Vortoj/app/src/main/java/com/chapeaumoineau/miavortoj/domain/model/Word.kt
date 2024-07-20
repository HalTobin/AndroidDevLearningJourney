package com.chapeaumoineau.miavortoj.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chapeaumoineau.miavortoj.domain.extensions.difference
import com.chapeaumoineau.miavortoj.domain.extensions.equalsCustom

@Entity
data class Word (
    val sourceWord: String,
    val targetWord: String,
    val emote: String,
    val notes: String,
    val timestamp: Long,
    val lastTestTimestamp: Long,
    val nbPlayed: Int = 0,
    val nbSucceed: Int = 0,
    val dictionaryId: Int,
    val themeId: Int,
    @PrimaryKey val id: Int? = null
) {

    fun getScore(): Long {
        val time = (System.currentTimeMillis() - lastTestTimestamp) / 360000

        val successRate = if((nbSucceed!=0) && (nbPlayed!=0)) 100 - ((nbSucceed / nbPlayed) * 100) else 100

        return time + successRate
    }

    /*fun isValid(input: String): Boolean {
        return targetWord.equalsCustom(input)
    }

    fun isClose(input: String): Boolean {
        return (targetWord.difference(input) < 3)
    }*/

}