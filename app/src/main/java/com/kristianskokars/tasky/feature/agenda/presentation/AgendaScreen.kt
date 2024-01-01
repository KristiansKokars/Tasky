package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.Orange
import com.kristianskokars.tasky.core.presentation.theme.White
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import com.kristianskokars.tasky.feature.agenda.presentation.components.AgendaCard
import com.kristianskokars.tasky.feature.agenda.presentation.components.ProfileIcon
import com.kristianskokars.tasky.feature.agenda.presentation.util.initial
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.datetime.LocalDateTime

@AppGraph(start = true)
@Destination
@Composable
fun AgendaScreen(viewModel: AgendaViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AgendaScreenContent(state = state, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreenContent(
    state: AgendaState,
    onEvent: (AgendaEvent) -> Unit
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
            FloatingActionButton(
                containerColor = Black,
                contentColor = White,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(
                        R.string.add_new_agenda_item
                    )
                )
            }
        }
    ) { padding ->
        TaskySurface(modifier = Modifier.padding(padding)) {
            DaySelection(
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

@Composable
private fun DaySelection(
    days: List<LocalDateTime>,
    selectedDayIndex: Int,
    onDayClick: (index: Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        itemsIndexed(days) { index, day ->
            Day(
                modifier = Modifier.clickable { onDayClick(index) },
                letter = day.dayOfWeek.initial(),
                number = day.dayOfMonth,
                isSelected = index == selectedDayIndex
            )
        }
    }
}

@Composable
private fun Day(modifier: Modifier = Modifier, letter: String, number: Int, isSelected: Boolean) {
    val selectedBackground = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(Orange)
    Column(
        modifier = modifier
            .then(if (isSelected) selectedBackground else Modifier)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = letter,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) DarkGray else Gray
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "$number", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DarkGray)
    }
}

@Preview
@Composable
private fun AgendaScreenPreview() {
    ScreenSurface {
        AgendaScreenContent(state = AgendaState(agendas = Agenda.previewValues), onEvent = {})
    }
}
