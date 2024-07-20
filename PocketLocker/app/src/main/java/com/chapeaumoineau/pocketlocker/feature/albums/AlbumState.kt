package com.chapeaumoineau.pocketlocker.feature.albums

import com.chapeaumoineau.pocketlocker.data.model.Album
import com.chapeaumoineau.pocketlocker.data.repository.StorageDestination

data class AlbumState(
    val albums: List<Album> = emptyList(),
    val newTitle: String = "",
    val newKey: String = "",
    val newDestination: StorageDestination = StorageDestination.Intern
)