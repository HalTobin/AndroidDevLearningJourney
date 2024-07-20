package com.chapeaumoineau.pocketlocker.feature.home_lock

sealed class HomeLockEvent {
    data class UnlockApp(val password: String): HomeLockEvent()
    data class SetField(val field: HomeLockField, val text: String): HomeLockEvent()
    data class CreatePassword(val password: String, val passwordCheck: String): HomeLockEvent()
}

enum class HomeLockField { PASSWORD, NEW_PASSWORD, CHECK_PASSWORD }