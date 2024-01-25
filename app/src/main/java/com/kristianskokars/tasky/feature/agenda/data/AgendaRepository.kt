package com.kristianskokars.tasky.feature.agenda.data

import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgendaRepository @Inject constructor(
    private val api: TaskyAPI,
    private val clock: Clock
) {
    private val _agendaCache = MutableStateFlow<List<Agenda>>(emptyList())
    private val _isLoadingAgendas = MutableStateFlow(false)

    val isLoadingAgendas = _isLoadingAgendas.asStateFlow()

    fun currentAgendas(): Flow<List<Agenda>> = _agendaCache

    suspend fun fetchAgendasForDay(timeZone: TimeZone, atInstant: Instant) {
        _isLoadingAgendas.update { true }
        val agendas = api.agendaForTheDay(
            timeZone.id,
            atInstant.toEpochMilliseconds()
        ).toAgendas(clock)
        _isLoadingAgendas.update { false }
        _agendaCache.update { agendas }
    }
}
