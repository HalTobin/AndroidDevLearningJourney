package com.chapeaumoineau.miavortoj.feature.quiz.presentation.quiz

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.chapeaumoineau.miavortoj.domain.model.Category
import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.model.Language
import com.chapeaumoineau.miavortoj.domain.use_case.DictionaryUseCases
import com.chapeaumoineau.miavortoj.domain.use_case.WordUseCases
import com.chapeaumoineau.miavortoj.feature.quiz.model.Answer
import com.chapeaumoineau.miavortoj.feature.quiz.model.GameSet
import com.chapeaumoineau.miavortoj.feature.quiz.model.Results
import com.chapeaumoineau.miavortoj.feature.quiz.model.Rules
import com.chapeaumoineau.miavortoj.feature.quiz.util.AnswerType
import com.chapeaumoineau.miavortoj.util.CustomTextToSpeech
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    application: Application,
    private val wordUseCases: WordUseCases,
    private val dictionaryUseCases: DictionaryUseCases,
    savedStateHandle: SavedStateHandle
): AndroidViewModel(application), CustomTextToSpeech.OnTextToSpeechReady {

    private val _answer = mutableStateOf(Answer())
    val answer: State<Answer> = _answer

    private val _dictionary = mutableStateOf(Dictionary("", "","", 0))
    val dictionary: State<Dictionary> = _dictionary

    private val _language = mutableStateOf(Language.getDefault())
    val language: State<Language> = _language

    private val _category = mutableStateOf(Category.getDefaultCategory())
    val category: State<Category> = _category

    private val _userEntry = mutableStateOf("")
    val userEntry: State<String> = _userEntry

    private val _progress = mutableStateOf(0.0f)
    val progress = _progress

    private val _isTtsAvailable = mutableStateOf(false)
    val isTtsAvailable = _isTtsAvailable

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var tts = CustomTextToSpeech(this)

    private val gameSet = GameSet(rules = Rules(duration = 10, categoryId = Rules.CATEGORY_ALL, difficulty = Rules.DIFFICULTY_ALL))

    private var errors = 0

    init {
        savedStateHandle.get<Int>("dictionaryId")?.let { dictionaryId ->
            if (dictionaryId != -1) {
                viewModelScope.launch {
                    dictionaryUseCases.getDictionary(dictionaryId)?.also { dictionaryFromDb ->
                        _dictionary.value = dictionaryFromDb
                        _language.value = Language.getLanguageByIso(dictionaryFromDb.languageIso)
                        tts.initTextToSpeech(getApplication<Application>().applicationContext, _language.value)
                        initializeGameSet()
                    }
                }
            }
        }
    }

    private suspend fun initializeGameSet() {
        _dictionary.value.id?.let {
            val words = wordUseCases.getWordsFromDictionary(it).first().toMutableList()
            gameSet.findAndSetGameWords(words, gameSet.rules.duration)
            _answer.value = gameSet.getCurrentAnswer()
        }
    }

    private fun proceed() {
        _userEntry.value = ""
        val next = gameSet.next()
        _progress.value = (gameSet.currentIndex).toFloat() / (gameSet.rules.duration).toFloat()
        if (next != null) _answer.value = next
        else {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.OpenResults(
                        Results(
                            duration = gameSet.getDuration(),
                            correctAnswers = gameSet.getDuration() - errors,
                            wrongAnswers = errors
                        )
                    )
                )
            }
        }
    }

    fun onEvent(event: QuizEvent) {
        when(event) {
            // EVENT FOR USER TEXT INPUT
            is QuizEvent.EnteredEntry -> {
                _userEntry.value = event.value
            }
            is QuizEvent.CheckAnswer -> {
                viewModelScope.launch {
                    when(_answer.value.getResult(_userEntry.value)) {
                        is AnswerType.GOOD -> {
                            _eventFlow.emit(UiEvent.AnswerValid)
                            _answer.value.wordId.let {
                                wordUseCases.changeWordLastTimestamp(it, System.currentTimeMillis())
                                wordUseCases.changeWordNbPlayed(it, _answer.value.played+1)
                                wordUseCases.changeWordNbSucceed(it, _answer.value.succeed+1)
                            }
                            proceed()
                        }
                        is AnswerType.CLOSE -> _eventFlow.emit(UiEvent.AnswerClose)
                        is AnswerType.BAD -> _eventFlow.emit(UiEvent.AnswerWrong)
                    }
                }
            }
            is QuizEvent.NextWord -> {
                viewModelScope.launch {
                    wordUseCases.changeWordNbPlayed(_answer.value.wordId, _answer.value.played+1)
                    errors += 1
                    proceed()
                }
            }
            is QuizEvent.SpeakWord -> if(_answer.value.isFromTarget) tts.speak(_answer.value.question)
            is QuizEvent.EndQuiz -> {
                viewModelScope.launch { _eventFlow.emit(UiEvent.CloseQuiz) }
            }
        }
    }

    sealed class UiEvent {
        object CloseQuiz: UiEvent()
        object AnswerValid: UiEvent()
        object AnswerClose: UiEvent()
        object AnswerWrong: UiEvent()
        data class OpenResults(val results: Results): UiEvent()
    }

    override fun onTextToSpeechReady(isTextToSpeechReady: Boolean) {
        _isTtsAvailable.value = isTextToSpeechReady
    }

}