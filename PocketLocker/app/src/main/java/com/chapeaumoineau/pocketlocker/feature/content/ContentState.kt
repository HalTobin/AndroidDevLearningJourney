package com.chapeaumoineau.pocketlocker.feature.content

import com.chapeaumoineau.pocketlocker.data.model.Album

data class ContentState(
    val album: Album? = null,
    val elements: List<String> = emptyList(),
    val loadingData: LoadingData? = null
)

data class LoadingData(
    val completed: Float,
    val current: Int,
    val max: Int
)