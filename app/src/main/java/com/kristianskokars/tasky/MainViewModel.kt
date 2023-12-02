package com.kristianskokars.tasky

import androidx.lifecycle.ViewModel
import com.kristianskokars.tasky.feature.auth.data.BackendAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor(authProvider: BackendAuthProvider) : ViewModel() {
    private val _isInitializingApp = MutableStateFlow(true)

    val authState = authProvider.authState
    val isInitializingApp = _isInitializingApp.asStateFlow()

    fun hasInitializedApp() {
        _isInitializingApp.update { false }
    }
}
