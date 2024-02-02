package com.kristianskokars.tasky.feature.agenda.presentation

import com.kristianskokars.tasky.feature.agenda.domain.model.Agenda
import com.kristianskokars.tasky.lib.currentSystemDate
import com.kristianskokars.tasky.lib.next6Days
import kotlinx.datetime.LocalDate
import java.util.Locale

data class AgendaState(
    val nameInitials: String = "",
    val currentChosenDate: LocalDate = currentSystemDate(),
    val locale: Locale = Locale.getDefault(),
    val currentWeekDays: List<LocalDate> = currentSystemDate().next6Days(),
    val agendas: List<Agenda> = emptyList(),
    val isLoadingAgendas: Boolean = false,
    val lastDoneAgendaId: String? = null,
    val selectedDayIndex: Int = 0
)
