package com.kristianskokars.tasky.feature.auth.domain

fun validatePassword(password: String): Boolean {
    if (password.length < 9) return false
    return password.contains(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+"))
}
