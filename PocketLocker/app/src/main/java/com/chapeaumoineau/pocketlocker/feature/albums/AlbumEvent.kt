package com.chapeaumoineau.pocketlocker.feature.albums

import com.chapeaumoineau.pocketlocker.data.repository.StorageDestination

sealed class AlbumEvent {
    data class OpenAlbum(val id: Int, val password: String): AlbumEvent()
    data class CreateAlbum(val name: String, val destination: StorageDestination, val password: String): AlbumEvent()
    data class UpdateField(val field: AlbumField): AlbumEvent()
}

sealed class AlbumField {
    data class NewTitle(val title: String): AlbumField()
    data class NewKey(val key: String): AlbumField()
    data class NewDestination(val destination: StorageDestination): AlbumField()
}