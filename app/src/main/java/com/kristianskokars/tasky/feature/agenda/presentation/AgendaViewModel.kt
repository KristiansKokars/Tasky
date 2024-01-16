package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kristianskokars.tasky.feature.agenda.data.AgendaRepository
import com.kristianskokars.tasky.core.presentation.util.nameOfMonth
import com.kristianskokars.tasky.feature.auth.data.BackendAuthProvider
import com.kristianskokars.tasky.lib.asStateFlow
import com.kristianskokars.tasky.lib.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
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
    private val currentWeekDays = clock.next6Days().map { it.toLocalDateTime(timeZone) }

    val state = combine(
        repository.currentAgendas(),
        _selectedDayIndex
    ) { agendas, selectedDayIndex ->
        val lastDoneAgenda = agendas.lastOrNull { it.isDone }?.id

        AgendaState(
            month = clock.now().toLocalDateTime(timeZone).nameOfMonth(locale).uppercase(locale),
            agendas = agendas.sortedBy { it.time },
            currentWeekDays = currentWeekDays,
            lastDoneAgenda = lastDoneAgenda,
            selectedDayIndex = selectedDayIndex
        )
    }.asStateFlow(viewModelScope, AgendaState())

    init {
        fetchAgendas()
    }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.DaySelected -> selectNewCurrentDay(event.dayIndex)
            AgendaEvent.Logout -> logout()
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

    private fun fetchAgendas() {
        launch {
            repository.fetchAgendas()
        }
    }

    private fun Clock.next6Days(): List<Instant> = List(6) { index ->
        now().plus(index, DateTimeUnit.DAY, timeZone)
    }
}
