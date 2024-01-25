package com.kristianskokars.tasky.feature.agenda.presentation

import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import com.kristianskokars.tasky.lib.currentSystemDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale

data class AgendaState(
    val currentChosenDate: LocalDate = currentSystemDate(),
    val locale: Locale = Locale.getDefault(),
    val currentWeekDays: List<LocalDate> = emptyList(),
    val agendas: List<Agenda> = emptyList(),
    val lastDoneAgenda: String? = null,
    val selectedDayIndex: Int = 0
)
