package com.chapeaumoineau.miavortoj.feature.quiz.util

sealed class AnswerType {
    object GOOD: AnswerType()
    object CLOSE: AnswerType()
    object BAD: AnswerType()
}