package com.chapeaumoineau.miavortoj.feature.words.presentation.words

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chapeaumoineau.miavortoj.domain.extensions.*
import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.model.Language
import com.chapeaumoineau.miavortoj.feature.quiz.model.Rules
import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.use_case.DictionaryUseCases
import com.chapeaumoineau.miavortoj.domain.use_case.WordUseCases
import com.chapeaumoineau.miavortoj.util.OrderType
import com.chapeaumoineau.miavortoj.feature.words.util.WordOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsViewModel @Inject constructor(private val wordUseCases: WordUseCases,
                                         private val dictionaryUseCases: DictionaryUseCases,
                                         savedStateHandle: SavedStateHandle): ViewModel() {

    private val _state = mutableStateOf(WordsState())
    val state: State<WordsState> = _state

    private var recentlyDeletedWord: Word? = null

    private var getWordsJob: Job? = null

    private val _dictionary = mutableStateOf(Dictionary.getDefault())
    val dictionary: State<Dictionary> = _dictionary

    private val _language = mutableStateOf(Language.getDefault())
    val language: State<Language> = _language

    private val _isFromSource = mutableStateOf(true)
    val isFromSource: State<Boolean> = _isFromSource

    private val _wordList = mutableStateOf(listOf<Word>())
    val words: State<List<Word>> = _wordList

    private val _wordSearch = mutableStateOf("")
    val search: State<String> = _wordSearch

    private val _rules = mutableStateOf(Rules.getDefault())
    val rules: State<Rules> = _rules

    private var currentDictionaryId: Int? = null

    init {
        _state.value = state.value.copy(wordEdit = -1)
        savedStateHandle.get<Int>("dictionaryId")?.let {dictionaryId ->
            if(dictionaryId != -1) {
                currentDictionaryId = dictionaryId
                viewModelScope.launch {
                    dictionaryUseCases.getDictionary(dictionaryId)?.also { dictionary ->
                        _dictionary.value = dictionary
                        _language.value = Language.getLanguageByIso(dictionary.languageIso)
                    }
                }
            }
        }
        getWords(currentDictionaryId, WordOrder.Source(OrderType.Ascending), "")
    }

    fun onEvent(event: WordsEvent) {
        when(event) {
            is WordsEvent.Order -> {
                if(state.value.wordOrder::class == event.wordOrder::class && state.value.wordOrder.orderType == event.wordOrder.orderType) {
                    return
                }
                getWords(currentDictionaryId, event.wordOrder, "")
                if (event.wordOrder is WordOrder.Source) _isFromSource.value = true
                if (event.wordOrder is WordOrder.Target) _isFromSource.value = false
                if (event.wordOrder is WordOrder.Theme) _isFromSource.value = true
            }

            //EVENT FOR SEARCH FIELD
            is WordsEvent.EnteredSearch -> {
                _wordSearch.value = event.value
                getWords(currentDictionaryId, state.value.wordOrder, event.value)
            }

            is WordsEvent.DeleteWord -> {
                _state.value = state.value.copy(wordEdit = -1)
                _state.value = state.value.copy(isDeleteDialogVisible = false)
                viewModelScope.launch {
                    wordUseCases.deleteWord(event.word)
                    recentlyDeletedWord = event.word
                }
            }
            is WordsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(isOrderSectionVisible = !state.value.isOrderSectionVisible)
            }
            is WordsEvent.ToggleEditMode -> {
                _state.value = event.wordId?.let { state.value.copy(wordEdit = it) }!!
            }

            //EVENT RELATED TO DELETE DIALOG
            is WordsEvent.ToggleDeleteDialog -> {
                _state.value = state.value.copy(wordEdit = -1)
                _state.value = state.value.copy(wordDelete = event.word)
                _state.value = state.value.copy(isDeleteDialogVisible = true)
            }
            is WordsEvent.ToggleWarningDialog -> _state.value = state.value.copy(isWarningDialogVisible = true)
            is WordsEvent.DismissWarningDialog -> _state.value = state.value.copy(isWarningDialogVisible = false)
            is WordsEvent.DismissDeleteDialog -> _state.value = state.value.copy(isDeleteDialogVisible = false)
            else -> {}
        }
    }

    private fun getWords(id: Int?, wordOrder: WordOrder, search: String) {
        getWordsJob?.cancel()
        getWordsJob = id?.let {
            wordUseCases.getWordsFromDictionary(it, wordOrder).onEach {
                words -> _state.value = state.value.copy(words = words.filter { l -> l.sourceWord.containsCustom(search) || l.targetWord.containsCustom(search) }, wordOrder = wordOrder)
            }.launchIn(viewModelScope)
        }
    }
}