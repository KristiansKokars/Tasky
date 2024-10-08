package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.data.local.model.CreateAgendaType
import com.kristianskokars.tasky.core.presentation.components.DatePickerDialog
import com.kristianskokars.tasky.core.presentation.components.LoadingSpinner
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.destinations.EventScreenDestination
import com.kristianskokars.tasky.destinations.ReminderScreenDestination
import com.kristianskokars.tasky.destinations.TaskScreenDestination
import com.kristianskokars.tasky.feature.agenda.domain.model.Agenda
import com.kristianskokars.tasky.feature.agenda.presentation.components.AddAgendaButton
import com.kristianskokars.tasky.feature.agenda.presentation.components.AgendaCard
import com.kristianskokars.tasky.feature.agenda.presentation.components.ProfileIcon
import com.kristianskokars.tasky.feature.agenda.presentation.components.TopDayRow
import com.kristianskokars.tasky.feature.event.presentation.event.EventScreenNavArgs
import com.kristianskokars.tasky.feature.reminder.presentation.ReminderScreenNavArgs
import com.kristianskokars.tasky.feature.task.presentation.TaskScreenNavArgs
import com.kristianskokars.tasky.lib.ObserveAsEvents
import com.kristianskokars.tasky.lib.formatToLongDate
import com.kristianskokars.tasky.lib.nameOfMonth
import com.kristianskokars.tasky.lib.showToast
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@AppGraph(start = true)
@Destination
@Composable
fun AgendaScreen(viewModel: AgendaViewModel = hiltViewModel(), navigator: DestinationsNavigator) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            AgendaViewModel.UIEvent.DeletedSuccessfully -> showToast(context, R.string.deleted_agenda)
            AgendaViewModel.UIEvent.ErrorDeleting -> showToast(context, R.string.failed_delete_agenda)
        }
    }
    AgendaScreenContent(state = state, onEvent = viewModel::onEvent, navigator)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgendaScreenContent(
    state: AgendaState,
    onEvent: (AgendaEvent) -> Unit,
    navigator: DestinationsNavigator
) {
    val editDateDialogState = rememberMaterialDialogState()

    DatePickerDialog(
        dialogState = editDateDialogState,
        onDateSelected = { date -> onEvent(AgendaEvent.OnDatePicked(date)) }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .clickable(
                                onClick = editDateDialogState::show,
                                onClickLabel = stringResource(R.string.change_date),
                                role = Role.Button,
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.currentChosenDate.nameOfMonth(state.locale).uppercase(),
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
                actions = { ProfileIcon(onLogOut = { onEvent(AgendaEvent.Logout) }, nameInitials = state.nameInitials) }
            )
        },
        floatingActionButton = {
            AddAgendaButton(
                onCreateNewAgenda = { agendaType ->
                    when (agendaType) {
                        CreateAgendaType.Event -> navigator.navigate(
                            EventScreenDestination(
                                EventScreenNavArgs(isCreatingNewEvent = true, isEditing = true)
                            )
                        )
                        CreateAgendaType.Reminder -> navigator.navigate(
                            ReminderScreenDestination(
                                ReminderScreenNavArgs(isCreatingNewReminder = true, isEditing = true)
                            )
                        )
                        CreateAgendaType.Task -> navigator.navigate(
                            TaskScreenDestination(
                                TaskScreenNavArgs(isCreatingNewTask = true, isEditing = true)
                            )
                        )
                    }
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
                Text(
                    text = state.currentWeekDays[state.selectedDayIndex].formatToLongDate(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Box {
                if (state.isLoadingAgendas) {
                    LoadingSpinner()
                } else {
                    AgendaList(
                        agendas = state.agendas,
                        lastDoneAgendaId = state.lastDoneAgendaId,
                        onTaskIsDone = { onEvent(AgendaEvent.OnTaskToggleDone(it)) },
                        onOpenClick = { agenda ->
                            when (agenda) {
                                is Agenda.Event -> navigator.navigate(EventScreenDestination(id = agenda.id))
                                is Agenda.Reminder -> navigator.navigate(ReminderScreenDestination(id = agenda.id))
                                is Agenda.Task -> navigator.navigate(TaskScreenDestination(id = agenda.id))
                            }
                        },
                        onEditClick = { agenda ->
                            when (agenda) {
                                is Agenda.Event -> navigator.navigate(EventScreenDestination(id = agenda.id, isEditing = true))
                                is Agenda.Reminder -> navigator.navigate(ReminderScreenDestination(id = agenda.id, isEditing = true))
                                is Agenda.Task -> navigator.navigate(TaskScreenDestination(id = agenda.id, isEditing = true))
                            }
                        },
                        onDeleteClick = { onEvent(AgendaEvent.DeleteAgenda(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AgendaList(
    agendas: List<Agenda>,
    lastDoneAgendaId: String?,
    onTaskIsDone: (taskId: String) -> Unit,
    onOpenClick: (agenda: Agenda) -> Unit,
    onEditClick: (agenda: Agenda) -> Unit,
    onDeleteClick: (agenda: Agenda) -> Unit,
) {
    LazyColumn(contentPadding = PaddingValues(bottom = 64.dp)) {
        item(key = "needle$lastDoneAgendaId") {
            if (agendas.isEmpty()) {
                Text(text = stringResource(R.string.no_agenda_items_scheduled), color = Black)
            }
            if (lastDoneAgendaId == null && agendas.isNotEmpty()) {
                Spacer(modifier = Modifier.size(8.dp))
                TimeNeedle()
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
        items(agendas, key = { it.id }) { agenda ->
            AgendaCard(
                agenda = agenda,
                onTaskIsDone = onTaskIsDone,
                onOpenClick = onOpenClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
            if (lastDoneAgendaId == agenda.id) {
                Spacer(modifier = Modifier.size(8.dp))
                TimeNeedle()
                Spacer(modifier = Modifier.size(8.dp))
            } else {
                Spacer(modifier = Modifier.size(16.dp))
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
