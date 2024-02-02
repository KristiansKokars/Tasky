package com.kristianskokars.tasky.feature.agenda.data

import com.kristianskokars.tasky.core.data.local.db.EventDao
import com.kristianskokars.tasky.core.data.local.db.ReminderDao
import com.kristianskokars.tasky.core.data.local.db.TaskDao
import com.kristianskokars.tasky.core.data.local.db.model.toAgendaEvent
import com.kristianskokars.tasky.core.data.local.db.model.toAgendaReminder
import com.kristianskokars.tasky.core.data.local.db.model.toAgendaTask
import com.kristianskokars.tasky.core.data.local.db.model.toDBModel
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

@Singleton
class AgendaRepository @Inject constructor(
    private val api: TaskyAPI,
    private val taskDao: TaskDao,
    private val eventDao: EventDao,
    private val reminderDao: ReminderDao,
    private val clock: Clock,
) {
    private val cacheTimeouts = mutableMapOf<Instant, Instant>()
    private val _isLoadingAgendas = MutableStateFlow(false)
    val isLoadingAgendas = _isLoadingAgendas.asStateFlow()

    fun currentAgendas(currentDate: LocalDate, timeZone: TimeZone): Flow<List<Agenda>> {
        val startingDayMillis = currentDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val endingDayMillis = currentDate.plus(DatePeriod(days = 1)).atStartOfDayIn(timeZone).toEpochMilliseconds()

        return combine(
            taskDao.getTasksForDay(startingDayMillis, endingDayMillis),
            eventDao.getEventsForDay(startingDayMillis, endingDayMillis),
            reminderDao.getRemindersForDay(startingDayMillis, endingDayMillis)
        ) { tasks, events, reminders ->
            val agendas = tasks.map { it.toAgendaTask() } + events.map { it.toAgendaEvent() } + reminders.map { it.toAgendaReminder() }
            agendas.sortedBy { it.time }
        }
    }

    suspend fun fetchAgendasForDay(timeZone: TimeZone, atInstant: Instant) {
        val cacheTimeoutForDay = cacheTimeouts[atInstant]
        if (cacheTimeoutForDay != null && clock.now() < cacheTimeoutForDay) return

        _isLoadingAgendas.update { true }
        val agendas = api.agendaForTheDay(
            timeZone.id,
            atInstant.toEpochMilliseconds()
        )
        cacheTimeouts[atInstant] = clock.now().plus(5.minutes)
        _isLoadingAgendas.update { false }
        taskDao.insertTasks(agendas.tasks.map { it.toDBModel() })
        eventDao.insertEvents(agendas.events.map { it.toDBModel() })
        reminderDao.insertReminders(agendas.reminders.map { it.toDBModel() })
    }
}
