package com.kristianskokars.tasky.feature.auth.domain

import android.util.Patterns

fun validateEmail(email: String) = email.contains(Patterns.EMAIL_ADDRESS.toRegex())
