package com.kristianskokars.tasky.feature.agenda.data

import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgendaRepository @Inject constructor(
    private val api: TaskyAPI,
    private val clock: Clock
) {
    private val _agendaCache = MutableStateFlow<List<Agenda>>(emptyList())

    fun currentAgendas(): Flow<List<Agenda>> = _agendaCache
    suspend fun fetchAgendas() {
        val agendas = api.agendaForTheDay(
            TimeZone.currentSystemDefault().id,
            clock.now().toEpochMilliseconds()
        ).toAgendas(clock)
        _agendaCache.update { agendas }
    }
}
