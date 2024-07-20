package com.chapeaumoineau.pocketlocker.feature.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chapeaumoineau.pocketlocker.data.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
): ViewModel() {

    private val _state = MutableStateFlow(AlbumState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var albumJob: Job? = null

    init {
        getAlbums()
    }

    fun onEvent(event: AlbumEvent) {
        when (event) {
            is AlbumEvent.CreateAlbum -> viewModelScope.launch(Dispatchers.IO) {
                albumRepository.createAlbum(event.name, event.destination, event.password)
            }
            is AlbumEvent.OpenAlbum -> viewModelScope.launch(Dispatchers.IO) {
                val album = albumRepository.getAlbumBydId(event.id, event.password)
                if (album != null) _eventFlow.emit(UiEvent.UnlockSuccess(event.id, event.password))
                else _eventFlow.emit(UiEvent.UnlockFail)
            }
            is AlbumEvent.UpdateField -> {
                when (event.field) {
                    is AlbumField.NewTitle -> _state.update { it.copy(newTitle = event.field.title) }
                    is AlbumField.NewKey -> _state.update { it.copy(newKey = event.field.key) }
                    is AlbumField.NewDestination -> _state.update { it.copy(newDestination = event.field.destination) }
                }
            }
        }
    }

    private fun getAlbums() {
        albumJob?.cancel()
        albumJob = viewModelScope.launch(Dispatchers.IO) {
            albumRepository.getAllAlbums().collect { albums -> _state.update { it.copy(albums = albums) }}
        }
    }

    sealed class UiEvent {
        data class UnlockSuccess(val albumId: Int, val key: String): UiEvent()
        object UnlockFail: UiEvent()
    }

}