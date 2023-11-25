package com.kristianskokars.tasky.feature.auth.data.model

import com.kristianskokars.tasky.core.data.local.model.User

sealed class AuthState {
    data object RetrievingFromStorage : AuthState()
    data object LoggedOut : AuthState()
    data class LoggedIn(val user: User) : AuthState()
}
