package com.chapeaumoineau.miavortoj.feature.quiz.presentation.quiz

sealed class QuizEvent {
    data class EnteredEntry(val value: String): QuizEvent()
    //data class ChangeEntryFocus(val focusState: FocusState): QuizEvent()
    object SpeakWord: QuizEvent()
    object CheckAnswer: QuizEvent()
    object NextWord: QuizEvent()
    object EndQuiz: QuizEvent()
}
