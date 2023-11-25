package com.kristianskokars.tasky.feature.auth.presentation.register

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterState(
    val name: String = "",
    val isNameValid: Boolean? = null,
    val email: String = "",
    val isEmailValid: Boolean? = null,
    val password: String = "",
    val isPasswordValid: Boolean? = null,
    val isPasswordVisible: Boolean = false,
    val registerResult: RegisterResult = RegisterResult.NoResult
) : Parcelable {
    val canRegister get() = isNameValid == true && isEmailValid == true && isPasswordValid == true
}
