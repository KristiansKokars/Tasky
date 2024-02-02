package com.kristianskokars.tasky.feature.auth.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class LoginError : Parcelable {
    data object ServerError : LoginError()
    data object NetworkError : LoginError()
    data object WriteToLocalStorageError : LoginError()
}
