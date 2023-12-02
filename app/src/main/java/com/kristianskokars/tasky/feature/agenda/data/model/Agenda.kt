package com.kristianskokars.tasky.feature.agenda.data.model

sealed class Agenda {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val time: String
    abstract val isDone: Boolean

    data class Task(
        override val id: String,
        override val title: String,
        override val time: String,
        override val description: String,
        override val isDone: Boolean
    ) : Agenda()

    data class Event(
        override val id: String,
        override val title: String,
        override val time: String,
        override val description: String,
        override val isDone: Boolean
    ) : Agenda()

    data class Reminder(
        override val id: String,
        override val title: String,
        override val time: String,
        override val description: String,
        override val isDone: Boolean
    ) : Agenda()

    companion object {
        val previewValues = listOf(
            Task(id = "0", title = "Project X", time = "Mar 5, 10:00", description = "Just work", isDone = true),
            Event(id = "1", title = "Meeting", time = "Mar 5, 10:00", description = "Amet minim mollit non deserunt", isDone = false),
            Reminder(id = "2", title = "Lunch break", time = "Mar 5, 10:00", description = "Just food", isDone = false),
            Reminder(id = "3", title = "Project X", time = "Mar 5, 10:00", description = "Amet minim mollit non deserunt ullamco est", isDone = false),
        )
    }
}
