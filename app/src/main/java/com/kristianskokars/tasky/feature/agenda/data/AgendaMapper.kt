package com.kristianskokars.tasky.feature.agenda.data

import com.kristianskokars.tasky.core.data.remote.model.AgendaResponseDTO
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import kotlinx.datetime.Clock

fun AgendaResponseDTO.toAgendas(clock: Clock): List<Agenda> = buildList {
    addAll(
        events.map {
            Agenda.Event(
                id = it.id,
                title = it.title,
                time = it.from,
                description = it.description,
                isDone = clock.now().toEpochMilliseconds() > it.to
            )
        }
    )
    addAll(
        reminders.map {
            Agenda.Reminder(
                id = it.id,
                title = it.title,
                time = it.time,
                description = it.description ?: "",
                isDone = clock.now().toEpochMilliseconds() > it.time
            )
        }
    )
    addAll(
        tasks.map {
            Agenda.Task(
                id = it.id,
                title = it.title,
                time = it.time,
                description = it.description ?: "",
                isDone = it.isDone
            )
        }
    )
}
