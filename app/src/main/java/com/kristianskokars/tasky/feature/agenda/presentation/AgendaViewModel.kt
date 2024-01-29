package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kristianskokars.tasky.core.data.TaskRepository
import com.kristianskokars.tasky.feature.agenda.data.AgendaRepository
import com.kristianskokars.tasky.feature.auth.data.BackendAuthProvider
import com.kristianskokars.tasky.lib.asStateFlow
import com.kristianskokars.tasky.lib.launch
import com.kristianskokars.tasky.lib.next6Days
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    clock: Clock,
    private val agendaRepository: AgendaRepository,
    private val taskRepository: TaskRepository,
    private val timeZone: TimeZone,
    private val locale: Locale,
    private val backendAuthProvider: BackendAuthProvider,
) : ViewModel() {
    private val _selectedDayIndex = MutableStateFlow(0)
    private val _startingDate = MutableStateFlow(clock.now().toLocalDateTime(timeZone).date)

    val state = combine(
        agendaRepository.currentAgendas(),
        agendaRepository.isLoadingAgendas,
        _startingDate,
        _selectedDayIndex
    ) { agendas, isLoadingAgendas, startingDate, selectedDayIndex ->
        val lastDoneAgenda = agendas.lastOrNull { it.isDone }?.id

        AgendaState(
            currentChosenDate = startingDate,
            agendas = agendas.sortedBy { it.time },
            currentWeekDays = startingDate.next6Days(),
            lastDoneAgendaId = lastDoneAgenda,
            isLoadingAgendas = isLoadingAgendas,
            selectedDayIndex = selectedDayIndex,
            locale = locale,
        )
    }.asStateFlow(viewModelScope, AgendaState())

    init {
        fetchAgendasForDay(_selectedDayIndex.value)
    }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.DaySelected -> selectNewCurrentDay(event.dayIndex)
            AgendaEvent.Logout -> logout()
            is AgendaEvent.OnDatePicked -> onDatePicked(event.date)
            is AgendaEvent.OnTaskToggleDone -> onTaskIsDone(event.taskId)
        }
    }

    private fun selectNewCurrentDay(newDayIndex: Int) {
        _selectedDayIndex.update { currentDayIndex ->
            if (currentDayIndex != newDayIndex) {
                fetchAgendasForDay(newDayIndex)
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
        _startingDate.update { newDate }
        _selectedDayIndex.update { 0 }
        fetchAgendasForDay(_selectedDayIndex.value)
    }

    private fun fetchAgendasForDay(currentDayIndex: Int) {
        val currentState = state.value
        val currentDayInstant = currentState.currentWeekDays[currentDayIndex].atStartOfDayIn(timeZone)
        launch {
            agendaRepository.fetchAgendasForDay(
                timeZone,
                atInstant = currentDayInstant
            )
        }
    }

    private fun onTaskIsDone(taskId: String) {
        launch {
            taskRepository.markTaskAsDone(taskId)
        }
    }
}
