package com.kristianskokars.tasky.feature.agenda.presentation

import com.kristianskokars.tasky.feature.agenda.domain.model.Agenda
import kotlinx.datetime.LocalDate

sealed class AgendaEvent {
    data class DaySelected(val dayIndex: Int) : AgendaEvent()
    data class OnDatePicked(val date: LocalDate) : AgendaEvent()
    data class OnTaskToggleDone(val taskId: String) : AgendaEvent()
    data class DeleteAgenda(val agenda: Agenda) : AgendaEvent()
    data object Logout : AgendaEvent()
}
