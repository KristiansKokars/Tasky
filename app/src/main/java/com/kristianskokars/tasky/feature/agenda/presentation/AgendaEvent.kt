package com.kristianskokars.tasky.feature.agenda.presentation

sealed class AgendaEvent {
    data class DaySelected(val dayIndex: Int) : AgendaEvent()
    data object Logout : AgendaEvent()
}
