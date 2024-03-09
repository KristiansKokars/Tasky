package com.kristianskokars.tasky.feature.auth.presentation.login

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
class LoginViewModel @Inject constructor(
    private val authProvider: BackendAuthProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val stateKey = "LoginState"
    val state = savedStateHandle.getStateFlow(stateKey, LoginState())

    fun onEvent(event: LoginEvent) {
        Timber.i("LoginViewModel Event: $event")
        when (event) {
            is LoginEvent.OnEmailChange -> onEmailChange(event.value)
            is LoginEvent.OnPasswordChange -> onPasswordChange(event.value)
            LoginEvent.TogglePasswordVisibility -> togglePasswordVisibility()
            LoginEvent.Login -> login()
        }
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

    private fun login() {
        val currentState = state.value
        if (currentState.isEmailValid != true || currentState.isPasswordValid != true) return

        savedStateHandle[stateKey] = state.value.copy(loginResult = LoginResult.NoResult, isLoggingIn = true)

        launch {
            authProvider.login(currentState.email, currentState.password).mapBoth(
                success = {
                    savedStateHandle[stateKey] = state.value.copy(
                        loginResult = LoginResult.Success,
                        isLoggingIn = false
                    )
                    Timber.d("Logged in successfully: $currentState")
                },
                failure = {
                    savedStateHandle[stateKey] = state.value.copy(
                        loginResult = LoginResult.Error(it),
                        isLoggingIn = false
                    )
                    Timber.d("Failed to login user: $currentState")
                }
            )
        }
    }
}
