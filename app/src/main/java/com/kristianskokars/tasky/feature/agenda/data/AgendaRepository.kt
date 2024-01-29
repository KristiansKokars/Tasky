package com.kristianskokars.tasky.feature.agenda.data

import com.kristianskokars.tasky.core.data.local.db.model.TaskDao
import com.kristianskokars.tasky.core.data.local.db.model.toAgendaTask
import com.kristianskokars.tasky.core.data.local.db.model.toDBModel
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class AgendaRepository @Inject constructor(
    private val api: TaskyAPI,
    private val taskDao: TaskDao,
) {
    private val _isLoadingAgendas = MutableStateFlow(false)
    val isLoadingAgendas = _isLoadingAgendas.asStateFlow()

    fun currentAgendas(currentDate: LocalDate, timeZone: TimeZone): Flow<List<Agenda>> {
        return taskDao
            .getTasksForDay(
                startingDayMillis = currentDate.atStartOfDayIn(timeZone).toEpochMilliseconds(),
                endingDayMillis = currentDate.plus(DatePeriod(days = 1)).atStartOfDayIn(timeZone).toEpochMilliseconds(),
            )
            .mapLatest { tasks -> tasks.map { it.toAgendaTask() } }
    }

    suspend fun fetchAgendasForDay(timeZone: TimeZone, atInstant: Instant) {
        _isLoadingAgendas.update { true }
        val agendas = api.agendaForTheDay(
            timeZone.id,
            atInstant.toEpochMilliseconds()
        )
        _isLoadingAgendas.update { false }
        taskDao.insertTasks(agendas.tasks.map { it.toDBModel() })
    }
}
