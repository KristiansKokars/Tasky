package com.kristianskokars.tasky.feature.agenda.presentation

import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import kotlinx.datetime.LocalDateTime

data class AgendaState(
    val month: String = "",
    val currentWeekDays: List<LocalDateTime> = emptyList(),
    val agendas: List<Agenda> = emptyList(),
    val lastDoneAgenda: String? = null,
    val selectedDayIndex: Int = 0
)
