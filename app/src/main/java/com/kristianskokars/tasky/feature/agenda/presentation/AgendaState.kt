package com.kristianskokars.tasky.feature.agenda.presentation

import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import com.kristianskokars.tasky.lib.currentSystemDate
import kotlinx.datetime.LocalDate
import java.util.Locale

data class AgendaState(
    val currentChosenDate: LocalDate = currentSystemDate(),
    val locale: Locale = Locale.getDefault(),
    val currentWeekDays: List<LocalDate> = emptyList(),
    val agendas: List<Agenda> = emptyList(),
    val isLoadingAgendas: Boolean = false,
    val lastDoneAgendaId: String? = null,
    val selectedDayIndex: Int = 0
)
