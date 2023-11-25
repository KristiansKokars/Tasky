package com.kristianskokars.tasky.feature.auth.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class RegisterError : Parcelable {
    data object ServerError : RegisterError()
    data object NetworkError : RegisterError()
}
