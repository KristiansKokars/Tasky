package com.kristianskokars.tasky.feature.auth.presentation.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginState(
    val email: String = "",
    val isEmailValid: Boolean? = null,
    val password: String = "",
    val isPasswordValid: Boolean? = null,
    val isPasswordVisible: Boolean = false,
    val isLoggingIn: Boolean = false,
    val loginResult: LoginResult = LoginResult.NoResult
) : Parcelable {
    val canLogin get() = isEmailValid == true && isPasswordValid == true
}
