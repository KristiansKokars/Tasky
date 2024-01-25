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
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val repository: AgendaRepository,
    private val timeZone: TimeZone,
    private val clock: Clock,
    private val locale: Locale,
    private val backendAuthProvider: BackendAuthProvider,
) : ViewModel() {
    private val _selectedDayIndex = MutableStateFlow(0)
    private val _localDate = MutableStateFlow(clock.now().toLocalDateTime(timeZone).date)

    val state = combine(
        repository.currentAgendas(),
        repository.isLoadingAgendas,
        _localDate,
        _selectedDayIndex
    ) { agendas, isLoadingAgendas, localDate, selectedDayIndex ->
        val lastDoneAgenda = agendas.lastOrNull { it.isDone }?.id

        AgendaState(
            currentChosenDate = localDate,
            agendas = agendas.sortedBy { it.time },
            currentWeekDays = localDate.next6Days(),
            lastDoneAgendaId = lastDoneAgenda,
            isLoadingAgendas = isLoadingAgendas,
            selectedDayIndex = selectedDayIndex,
            locale = locale,
        )
    }.asStateFlow(viewModelScope, AgendaState())

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.DaySelected -> selectNewCurrentDay(event.dayIndex)
            AgendaEvent.Logout -> logout()
            is AgendaEvent.OnDatePicked -> onDatePicked(event.date)
            AgendaEvent.FetchAgendasForDay -> fetchAgendasForDay()
        }
    }

    private fun selectNewCurrentDay(newDayIndex: Int) {
        _selectedDayIndex.update { currentDayIndex ->
            if (currentDayIndex != newDayIndex) {
                fetchAgendasForDay()
            }
            newDayIndex
        }
    }

    private fun logout() {
        launch {
            backendAuthProvider.logout()
        }
    }

    private fun onDatePicked(newDate: LocalDate) {
        _localDate.update { newDate }
    }

    private fun fetchAgendasForDay() {
        launch {
            repository.fetchAgendasForDay(
                timeZone,
                atInstant = _localDate.value.next6Days()[_selectedDayIndex.value].atStartOfDayIn(timeZone)
            )
        }
    }

    private fun LocalDate.next6Days(): List<LocalDate> = List(6) { index ->
        this.plus(index, DateTimeUnit.DAY)
    }
}
