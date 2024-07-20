package com.chapeaumoineau.pocketlocker.feature.content

import android.net.Uri

sealed class ContentEvent {
    data class AddElements(val uriList: List<Uri>, val name: String): ContentEvent()
    data class DeleteElement(val path: String): ContentEvent()
}