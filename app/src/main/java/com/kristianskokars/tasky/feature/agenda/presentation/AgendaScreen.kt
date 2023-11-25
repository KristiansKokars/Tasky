package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination

@AppGraph(start = true)
@Destination
@Composable
fun AgendaScreen() {
    Text(text = "Agenda Screen Placeholder")
}
