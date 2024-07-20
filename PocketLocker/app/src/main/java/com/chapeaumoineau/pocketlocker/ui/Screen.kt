package com.chapeaumoineau.pocketlocker.ui

sealed class Screen(val route: String) {
    object HomeLock: Screen("home_lock")
    object HomeAlbum: Screen("home_album")
    object AlbumContent: Screen("album_content")
}