package com.kristianskokars.tasky.feature.auth.presentation.register

import android.os.Parcelable
import com.kristianskokars.tasky.feature.auth.data.model.RegisterError
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class RegisterResult : Parcelable {
    data object NoResult : RegisterResult()
    data object Success : RegisterResult()
    data class Error(val error: RegisterError) : RegisterResult()
}
