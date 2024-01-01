package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import com.kristianskokars.tasky.feature.agenda.presentation.components.AddAgendaButton
import com.kristianskokars.tasky.feature.agenda.presentation.components.AgendaCard
import com.kristianskokars.tasky.feature.agenda.presentation.components.ProfileIcon
import com.kristianskokars.tasky.feature.agenda.presentation.components.TopDayRow
import com.kristianskokars.tasky.feature.destinations.CreateAgendaScreenDestination
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@AppGraph(start = true)
@Destination
@Composable
fun AgendaScreen(viewModel: AgendaViewModel = hiltViewModel(), navigator: DestinationsNavigator) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AgendaScreenContent(state = state, onEvent = viewModel::onEvent, navigator)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgendaScreenContent(
    state: AgendaState,
    onEvent: (AgendaEvent) -> Unit,
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.month,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_open_date_dropdown),
                            contentDescription = null
                        )
                    }
                },
                actions = { ProfileIcon(onLogOut = { onEvent(AgendaEvent.Logout) }) }
            )
        },
        floatingActionButton = {
            AddAgendaButton(
                onCreateNewAgenda = { agendaType ->
                    navigator.navigate(CreateAgendaScreenDestination(agendaType))
                }
            )
        }
    ) { padding ->
        TaskySurface(modifier = Modifier.padding(padding)) {
            TopDayRow(
                days = state.currentWeekDays,
                selectedDayIndex = state.selectedDayIndex,
                onDayClick = { onEvent(AgendaEvent.DaySelected(it)) }
            )
            Spacer(modifier = Modifier.size(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Today", style = MaterialTheme.typography.headlineMedium)
            }
            Box {
                LazyColumn(
                    contentPadding = PaddingValues(top = 20.dp)
                ) {
                    item(key = state.lastDoneAgenda) {
                        if (state.lastDoneAgenda == null) {
                            TimeNeedle()
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                    }
                    items(state.agendas) { agenda ->
                        AgendaCard(agenda = agenda)
                        if (state.lastDoneAgenda == agenda.id) {
                            Spacer(modifier = Modifier.size(8.dp))
                            TimeNeedle()
                            Spacer(modifier = Modifier.size(8.dp))
                        } else {
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeNeedle() {
    Box(
        modifier = Modifier
            .background(Black)
            .fillMaxWidth()
            .height(2.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(Black, radius = 15f, center = Offset(0f, this.center.y))
        }
    }
}

@Preview
@Composable
private fun AgendaScreenPreview() {
    ScreenSurface {
        AgendaScreenContent(
            state = AgendaState(agendas = Agenda.previewValues),
            onEvent = {},
            navigator = EmptyDestinationsNavigator
        )
    }
}
