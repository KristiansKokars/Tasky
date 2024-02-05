package com.kristianskokars.tasky.core.presentation.util

/** Displays all the initials of a name, or if there is only one word, displays the first 2 letters of that word. */
fun String.nameInitials(): String {
    val words = split(" ")
    if (words.size == 1) {
        val name = words[0]
        return "${name[0]}${name[1]}".uppercase()
    }

    return words.map { it.first() }.joinToString("").uppercase()
}
