package com.chapeaumoineau.pocketlocker.feature.home_lock

data class HomeLockState(
    val entry: String = "",
    val isFirst: Boolean = false,
    val new1: String = "",
    val new2: String = ""
)