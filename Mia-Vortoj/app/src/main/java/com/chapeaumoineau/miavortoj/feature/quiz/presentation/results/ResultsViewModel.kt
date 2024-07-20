package com.chapeaumoineau.miavortoj.feature.quiz.presentation.results

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.chapeaumoineau.miavortoj.feature.quiz.model.Results
import com.chapeaumoineau.miavortoj.feature.quiz.presentation.quiz.QuizViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class ResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _results = mutableStateOf(Results())
    val results = _results

    private val _eventFlow = MutableSharedFlow<QuizViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("nbQuestions")?.let { nbQuestions ->
            if (nbQuestions != -1) {
                _results.value.duration = nbQuestions
            }
        }
        savedStateHandle.get<Int>("nbErrors")?.let { nbErrors ->
            if (nbErrors != -1) {
                _results.value.wrongAnswers = nbErrors
            }
        }
        savedStateHandle.get<Int>("nbCorrects")?.let { nbCorrects ->
            if (nbCorrects != -1) {
                _results.value.correctAnswers = nbCorrects
            }
        }
    }

}