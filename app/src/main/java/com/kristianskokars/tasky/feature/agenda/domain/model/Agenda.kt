package com.kristianskokars.tasky.feature.agenda.domain.model

sealed class Agenda {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val atTime: Long
    abstract val isDone: Boolean

    data class Task(
        override val id: String,
        override val title: String,
        override val atTime: Long,
        override val description: String,
        override val isDone: Boolean,
    ) : Agenda()

    data class Event(
        override val id: String,
        override val title: String,
        override val atTime: Long,
        override val description: String,
        override val isDone: Boolean,
        val toTime: Long
    ) : Agenda()

    data class Reminder(
        override val id: String,
        override val title: String,
        override val atTime: Long,
        override val description: String,
        override val isDone: Boolean
    ) : Agenda()

    companion object {
        val previewValues = listOf(
            Task(
                id = "0",
                title = "Project X",
                atTime = 1678003200000L,
                description = "Just work",
                isDone = true
            ),
            Event(
                id = "1",
                title = "Meeting",
                atTime = 1678003200000L,
                toTime = 1678004200000L,
                description = "Amet minim mollit non deserunt",
                isDone = false
            ),
            Reminder(
                id = "2",
                title = "Lunch break",
                atTime = 1678003200000L,
                description = "Just food",
                isDone = false
            ),
            Reminder(
                id = "3",
                title = "Project X",
                atTime = 1678003200000L,
                description = "Amet minim mollit non deserunt ullamco est",
                isDone = false
            )
        )
    }
}
