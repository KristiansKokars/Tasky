package com.kristianskokars.tasky.core.domain

object DeepLinks {
    // Remember to edit the manifest for the deep link receiver link too!
    private const val TASKY_DEEP_LINK_URL = "https://www.tasky.com"

    const val reminderPattern = "$TASKY_DEEP_LINK_URL/reminder/{id}"
    const val taskPattern = "$TASKY_DEEP_LINK_URL/task/{id}"
    const val eventPattern = "$TASKY_DEEP_LINK_URL/event/{id}"

    fun item(id: String, type: Type) = "$TASKY_DEEP_LINK_URL/$type/$id"

    enum class Type {
        REMINDER, TASK, EVENT;

        override fun toString(): String = when (this) {
            REMINDER -> "reminder"
            TASK -> "task"
            EVENT -> "event"
        }

        fun toPair(): Pair<String, String> = Type.KEY to toString()

        companion object {
            const val ID = "id"
            const val KEY = "type"
        }
    }

    enum class Extra {
        NAME, TIME;

        override fun toString(): String = when (this) {
            NAME -> "name"
            TIME -> "time"
        }
    }
}

fun String.toDeepLinkType() = when (this) {
    "reminder" -> DeepLinks.Type.REMINDER
    "task" -> DeepLinks.Type.TASK
    "event" -> DeepLinks.Type.EVENT
    else -> null
}
