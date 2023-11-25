package com.kristianskokars.tasky.feature.auth.presentation.register

sealed class RegisterEvent {
    data class OnNameChange(val value: String) : RegisterEvent()
    data class OnEmailChange(val value: String) : RegisterEvent()
    data class OnPasswordChange(val value: String) : RegisterEvent()
    data object TogglePasswordVisibility : RegisterEvent()
    data object Register : RegisterEvent()
}
