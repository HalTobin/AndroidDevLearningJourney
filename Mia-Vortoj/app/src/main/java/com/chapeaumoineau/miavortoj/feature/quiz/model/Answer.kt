package com.chapeaumoineau.miavortoj.feature.quiz.model

import com.chapeaumoineau.miavortoj.domain.extensions.difference
import com.chapeaumoineau.miavortoj.domain.extensions.equalsCustom
import com.chapeaumoineau.miavortoj.feature.quiz.util.AnswerType

data class Answer(
    val isFromTarget: Boolean = false,
    val wordId: Int = -1,
    val question: String = "",
    val answer: String = "",
    val emote: String = "",
    val played: Int = -1,
    val succeed: Int = -1
) {

    fun getResult(input: String): AnswerType {
        return if(isValid(input)) AnswerType.GOOD
        else {
            if(isClose(input)) AnswerType.CLOSE
            else AnswerType.BAD
        }
    }

    private fun isValid(input: String): Boolean {
        return answer.equalsCustom(input)
    }

    private fun isClose(input: String): Boolean {
        return (answer.difference(input) < 3)
    }

}