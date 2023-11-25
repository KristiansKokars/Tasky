package com.kristianskokars.tasky

import androidx.lifecycle.ViewModel
import com.kristianskokars.tasky.feature.auth.data.BackendAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(authProvider: BackendAuthProvider) : ViewModel() {
    val authState = authProvider.authState
}
