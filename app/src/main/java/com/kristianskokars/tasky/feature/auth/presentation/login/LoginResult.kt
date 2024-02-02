package com.kristianskokars.tasky.feature.auth.presentation.login

import android.os.Parcelable
import com.kristianskokars.tasky.feature.auth.domain.model.LoginError
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class LoginResult : Parcelable {
    data object NoResult : LoginResult()
    data object Success : LoginResult()
    data class Error(val error: LoginError) : LoginResult()
}
