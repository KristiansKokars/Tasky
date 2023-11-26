package com.kristianskokars.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kristianskokars.tasky.feature.auth.data.BackendAuthProvider
import com.kristianskokars.tasky.feature.auth.data.model.AuthState
import com.kristianskokars.tasky.lib.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map

@HiltViewModel
class MainViewModel @Inject constructor(authProvider: BackendAuthProvider) : ViewModel() {
    val authState = authProvider.authState
    val isLoadingUserSession = authState.map { authState ->
        authState == AuthState.RetrievingFromStorage
    }.asStateFlow(viewModelScope, true, SharingStarted.Eagerly)
}
