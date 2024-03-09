package com.kristianskokars.tasky.feature.auth.presentation.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.mapBoth
import com.kristianskokars.tasky.feature.auth.data.BackendAuthProvider
import com.kristianskokars.tasky.feature.auth.domain.validateEmail
import com.kristianskokars.tasky.feature.auth.domain.validatePassword
import com.kristianskokars.tasky.lib.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authProvider: BackendAuthProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val stateKey = "RegisterState"
    val state = savedStateHandle.getStateFlow(stateKey, RegisterState())

    fun onEvent(event: RegisterEvent) {
        Timber.i("RegisterViewModel Event: $event")
        when (event) {
            is RegisterEvent.OnNameChange -> onNameChange(event.value)
            is RegisterEvent.OnEmailChange -> onEmailChange(event.value)
            is RegisterEvent.OnPasswordChange -> onPasswordChange(event.value)
            RegisterEvent.TogglePasswordVisibility -> togglePasswordVisibility()
            RegisterEvent.Register -> register()
        }
    }

    private fun onNameChange(newName: String) {
        // TODO: may want to debounce for UX or wait until register call is made to tell an error?
        savedStateHandle[stateKey] = state.value.copy(
            name = newName,
            isNameValid = newName.length in 4..50
        )
    }

    private fun onEmailChange(newEmail: String) {
        savedStateHandle[stateKey] = state.value.copy(
            email = newEmail,
            isEmailValid = validateEmail(newEmail)
        )
    }

    private fun onPasswordChange(newPassword: String) {
        savedStateHandle[stateKey] = state.value.copy(
            password = newPassword,
            isPasswordValid = validatePassword(newPassword)
        )
    }

    private fun togglePasswordVisibility() {
        savedStateHandle[stateKey] = state.value.copy(
            isPasswordVisible = !state.value.isPasswordVisible
        )
    }

    private fun register() {
        val currentState = state.value
        if (currentState.isNameValid != true || currentState.isEmailValid != true || currentState.isPasswordValid != true) return

        launch {
            savedStateHandle[stateKey] = state.value.copy(isRegistering = true)
            authProvider.register(
                name = currentState.name,
                email = currentState.email,
                password = currentState.password
            ).mapBoth(
                success = {
                    savedStateHandle[stateKey] = state.value.copy(
                        registerResult = RegisterResult.Success,
                        isRegistering = false
                    )
                    Timber.d("Registered successfully: $currentState")
                },
                failure = {
                    savedStateHandle[stateKey] = state.value.copy(
                        registerResult = RegisterResult.Error(it),
                        isRegistering = false
                    )
                    Timber.d("Failed to register user: $currentState")
                }
            )
        }
    }
}
