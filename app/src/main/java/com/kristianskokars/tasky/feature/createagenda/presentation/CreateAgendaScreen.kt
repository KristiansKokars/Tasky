package com.kristianskokars.tasky.feature.createagenda.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kristianskokars.tasky.core.data.local.model.CreateAgendaType
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination

@AppGraph
@Destination
@Composable
fun CreateAgendaScreen(
    agendaType: CreateAgendaType,
) {
    Text(text = "$agendaType")
}
