package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kristianskokars.tasky.core.data.TaskRepository
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.feature.agenda.data.AgendaRepository
import com.kristianskokars.tasky.feature.auth.data.BackendAuthProvider
import com.kristianskokars.tasky.lib.asStateFlow
import com.kristianskokars.tasky.lib.launch
import com.kristianskokars.tasky.lib.next6Days
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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
    userStore: DataStore<UserSettings>,
    private val agendaRepository: AgendaRepository,
    private val taskRepository: TaskRepository,
    private val timeZone: TimeZone,
    private val locale: Locale,
    private val backendAuthProvider: BackendAuthProvider
) : ViewModel() {
    private val _selectedDayIndex = MutableStateFlow(0)
    private val _startingDate = MutableStateFlow(clock.now().toLocalDateTime(timeZone).date)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = combine(
        _startingDate.flatMapLatest { startingDate ->
            _selectedDayIndex.flatMapLatest { selectedDayIndex ->
                agendaRepository.currentAgendas(startingDate.next6Days()[selectedDayIndex], timeZone)
            }
        },
        agendaRepository.isLoadingAgendas,
        _startingDate,
        _selectedDayIndex,
        userStore.data
    ) { currentAgendas, isLoadingAgendas, startingDate, selectedDayIndex, user ->
        val lastDoneAgenda = currentAgendas.lastOrNull { it.isDone }?.id

        AgendaState(
            nameInitials = user.fullName?.nameInitials() ?: "",
            currentChosenDate = startingDate,
            agendas = currentAgendas,
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
            is AgendaEvent.DeleteAgenda -> deleteAgenda(event.id)
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
        val currentDayInstant =
            currentState.currentWeekDays[currentDayIndex].atStartOfDayIn(timeZone)
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

    private fun deleteAgenda(id: String) {
        // TODO
    }

    private fun String.nameInitials(): String {
        val words = split(" ")
        if (words.size == 1) {
            val name = words[0]
            return "${name[0]}${name[1]}".uppercase()
        }

        return words.map { it.first() }.joinToString("").uppercase()
    }
}
