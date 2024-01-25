package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kristianskokars.tasky.feature.agenda.data.AgendaRepository
import com.kristianskokars.tasky.feature.auth.data.BackendAuthProvider
import com.kristianskokars.tasky.lib.asStateFlow
import com.kristianskokars.tasky.lib.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    timeZone: TimeZone,
    clock: Clock,
    private val repository: AgendaRepository,
    private val locale: Locale,
    private val backendAuthProvider: BackendAuthProvider,
) : ViewModel() {
    private val _selectedDayIndex = MutableStateFlow(0)
    private val _localDate = MutableStateFlow(clock.now().toLocalDateTime(timeZone).date)

    val state = combine(
        repository.currentAgendas(),
        _localDate,
        _selectedDayIndex
    ) { agendas, localDate, selectedDayIndex ->
        val lastDoneAgenda = agendas.lastOrNull { it.isDone }?.id

        AgendaState(
            currentChosenDate = localDate,
            agendas = agendas.sortedBy { it.time },
            currentWeekDays = localDate.next6Days(),
            lastDoneAgenda = lastDoneAgenda,
            selectedDayIndex = selectedDayIndex,
            locale = locale,
        )
    }.asStateFlow(viewModelScope, AgendaState())

    init {
        fetchAgendas()
    }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.DaySelected -> selectNewCurrentDay(event.dayIndex)
            AgendaEvent.Logout -> logout()
            is AgendaEvent.OnDatePicked -> onDatePicked(event.date)
        }
    }

    private fun selectNewCurrentDay(dayIndex: Int) {
        _selectedDayIndex.update { dayIndex }
    }

    private fun logout() {
        launch {
            backendAuthProvider.logout()
        }
    }

    private fun onDatePicked(newDate: LocalDate) {
        _localDate.update { newDate }
    }

    private fun fetchAgendas() {
        launch {
            repository.fetchAgendas()
        }
    }

    private fun LocalDate.next6Days(): List<LocalDate> = List(6) { index ->
        this.plus(index, DateTimeUnit.DAY)
    }
}
