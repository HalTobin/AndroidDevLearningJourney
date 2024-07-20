package com.chapeaumoineau.pocketlocker.feature.home_lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chapeaumoineau.pocketlocker.data.repository.SecretPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeLockViewModel @Inject constructor(
    private val spRepository: SecretPreferenceRepository
): ViewModel() {

    private val _state = MutableStateFlow(HomeLockState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        _state.update { it.copy(isFirst = !spRepository.isPinExisting()) }
    }

    fun onEvent(event: HomeLockEvent) {
        when (event) {
            is HomeLockEvent.UnlockApp -> viewModelScope.launch {
                if (spRepository.isPinValid(event.password))
                    _eventFlow.emit(UiEvent.UnlockSuccess)
            }
            is HomeLockEvent.SetField -> {
                when (event.field) {
                    HomeLockField.PASSWORD -> _state.update { it.copy(entry = event.text) }
                    HomeLockField.NEW_PASSWORD -> _state.update { it.copy(new1 = event.text) }
                    HomeLockField.CHECK_PASSWORD -> _state.update { it.copy(new2 = event.text) }
                }
            }
            is HomeLockEvent.CreatePassword -> viewModelScope.launch {
                if (event.password == event.passwordCheck) {
                    spRepository.setPin(event.password)
                    _eventFlow.emit(UiEvent.UnlockSuccess)
                }
                else _eventFlow.emit(UiEvent.NotMatching)
            }
        }
    }

    sealed class UiEvent {
        object UnlockSuccess: UiEvent()
        object UnlockError: UiEvent()
        object NotMatching: UiEvent()
    }

}