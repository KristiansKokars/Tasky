package com.kristianskokars.tasky.feature.auth.domain.model

import com.kristianskokars.tasky.core.domain.model.User

sealed class AuthState {
    data object RetrievingFromStorage : AuthState()
    data object LoggedOut : AuthState()
    data class LoggedIn(val user: User) : AuthState()
}
