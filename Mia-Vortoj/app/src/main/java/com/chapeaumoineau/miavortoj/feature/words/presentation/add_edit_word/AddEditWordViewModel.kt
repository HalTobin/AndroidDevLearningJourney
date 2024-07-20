package com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chapeaumoineau.miavortoj.domain.extensions.containsCustom
import com.chapeaumoineau.miavortoj.domain.model.Category
import com.chapeaumoineau.miavortoj.domain.model.InvalidDictionaryException
import com.chapeaumoineau.miavortoj.domain.model.Language
import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.domain.use_case.DictionaryUseCases
import com.chapeaumoineau.miavortoj.domain.use_case.WordUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWordViewModel @Inject constructor(private val wordUseCases: WordUseCases,
                                               private val dictionaryUseCases: DictionaryUseCases,
                                               savedStateHandle: SavedStateHandle): ViewModel() {

    private var getWordJob: Job? = null

    private val _wordSource = mutableStateOf("")
    val source: State<String> = _wordSource

    private val _wordTarget = mutableStateOf("")
    val target: State<String> = _wordTarget

    private val _wordEmote = mutableStateOf("")
    val emote: State<String> = _wordEmote

    private val _wordNotes = mutableStateOf("")
    val notes: State<String> = _wordNotes

    private val _wordCategory = mutableStateOf(Category.getDefaultCategory())
    val category: State<Category> = _wordCategory

    private val _wordTargetHint = mutableStateOf("")
    val targetHint: State<String> = _wordTargetHint

    private val _color = mutableStateOf(Language.getDefault().getDarkColor())
    val color: State<Color> = _color

    private val _isCategoryDialogVisible = mutableStateOf(false)
    val dialog: State<Boolean> = _isCategoryDialogVisible

    private val _categorySearch = mutableStateOf("")
    val search: State<String> = _categorySearch

    private val _listFromClass = Category.defaultCategories
    val categoriesWithTranslation: State<List<Category>> = mutableStateOf(_listFromClass)
    private val _categoryList = mutableStateOf(_listFromClass)
    val categories: State<List<Category>> = _categoryList

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentWordId: Int? = null

    private var currentDictionaryId: Int? = null

    init {
        savedStateHandle.get<Int>("dictionaryId")?.let { dictionaryId ->
            if (dictionaryId != -1) {
                currentDictionaryId = dictionaryId
                viewModelScope.launch {
                    dictionaryUseCases.getDictionary(dictionaryId)?.also { dictionary ->
                        _color.value = Language.getLanguageByIso(dictionary.languageIso).getDarkColor()
                        _wordTargetHint.value = Language.getLanguageByIso(dictionary.languageIso).name
                    }
                }
            }
        }
        savedStateHandle.get<Int>("wordId")?.let { wordId ->
            if (wordId != -1) {
                getWord(wordId)
                /*viewModelScope.launch {
                    wordUseCases.getWord(wordId)?.also { word ->
                        currentWordId = word.id
                        _wordSource.value = word.sourceWord
                        _wordTarget.value = word.targetWord
                        _wordEmote.value = word.emote
                        _wordNotes.value = word.notes
                        _wordCategory.value = Category.getCategoryById(word.themeId)
                        _wordDifficulty.value = word.difficulty
                    }
                }*/
            }
        }
    }

    fun onEvent(event: AddEditWordEvent) {
        when(event) {
            // EVENT FOR SOURCE TEXT INPUT
            is AddEditWordEvent.EnteredSource -> {
                _wordSource.value = event.value
            }

            // EVENT FOR TARGET TEXT INPUT
            is AddEditWordEvent.EnteredTarget -> {
                _wordTarget.value = event.value
            }

            //EVENT FOR DESCRIPTION TEXT INPUT
            is AddEditWordEvent.EnteredDescription -> {
                _wordNotes.value = event.value
            }

            //EVENT FOR EMOTE TEXT INPUT
            is AddEditWordEvent.EnteredEmote -> {
                _wordEmote.value = event.value
            }

            is AddEditWordEvent.EnteredSearch -> {
                _categorySearch.value = event.value
                _categoryList.value = _listFromClass.sortedBy { it.translation }.filter { l -> l.title.containsCustom(event.value) || l.translation.containsCustom(event.value)}
            }

            is AddEditWordEvent.OnCategorySelected -> {
                _isCategoryDialogVisible.value = false
                _wordCategory.value = Category.getCategoryById(event.category)
            }

            is AddEditWordEvent.DismissCategoryDialog -> {
                _isCategoryDialogVisible.value = false
            }

            is AddEditWordEvent.SaveWord -> {
                viewModelScope.launch {
                    try {
                        wordUseCases.addWord(Word(sourceWord = source.value,
                            targetWord = target.value,
                            emote = emote.value,
                            notes = notes.value,
                            themeId = category.value.id,
                            dictionaryId = currentDictionaryId!!,
                            timestamp = System.currentTimeMillis(),
                            lastTestTimestamp = System.currentTimeMillis(),
                            id = currentWordId))
                        _eventFlow.emit(UiEvent.SaveWord)
                    } catch(e: InvalidDictionaryException) {
                        _eventFlow.emit(UiEvent.ShowSnackBar(message = e.message ?: "Couldn't saved word"))
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveWord: UiEvent()
    }

    private fun getWord(id: Int?) {
        getWordJob?.cancel()
        getWordJob = id?.let {
            wordUseCases.getWord(id)?.onEach { word ->
                currentWordId = word.id
                _wordSource.value = word.sourceWord
                _wordTarget.value = word.targetWord
                _wordEmote.value = word.emote
                _wordNotes.value = word.notes
                _wordCategory.value = Category.getCategoryById(word.themeId)
            }?.launchIn(viewModelScope)
        }
    }

}