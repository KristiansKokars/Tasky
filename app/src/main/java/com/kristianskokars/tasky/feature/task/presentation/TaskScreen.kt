package com.kristianskokars.tasky.feature.task.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyDivider
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.Green
import com.kristianskokars.tasky.feature.destinations.EditDescriptionScreenDestination
import com.kristianskokars.tasky.feature.destinations.EditTitleScreenDestination
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaBadge
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaDescription
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaTopBar
import com.kristianskokars.tasky.feature.event.presentation.components.EventTitle
import com.kristianskokars.tasky.feature.event.presentation.components.RemindBeforeSection
import com.kristianskokars.tasky.feature.event.presentation.components.TaskyTimeSection
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@AppGraph
@Destination
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    editTitleResultRecipient: ResultRecipient<EditTitleScreenDestination, String>,
    editDescriptionResultRecipient: ResultRecipient<EditDescriptionScreenDestination, String>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    editTitleResultRecipient.onNavResult { titleResult ->
        when (titleResult) {
            NavResult.Canceled -> { /* Ignored */ }
            is NavResult.Value -> viewModel.onEvent(TaskScreenEvent.OnTitleChanged(titleResult.value))
        }
    }
    editDescriptionResultRecipient.onNavResult { descriptionResult ->
        when (descriptionResult) {
            NavResult.Canceled -> { /* Ignored */ }
            is NavResult.Value -> viewModel.onEvent(
                TaskScreenEvent.OnDescriptionChanged(
                    descriptionResult.value
                )
            )
        }
    }

    TaskScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        navigator = navigator
    )
}

@Composable
private fun TaskScreenContent(
    state: TaskScreenState,
    onEvent: (TaskScreenEvent) -> Unit,
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            AgendaTopBar(
                isEditing = state.isEditing,
                isEditingTitle = stringResource(R.string.edit_task),
                onSaveClick = { onEvent(TaskScreenEvent.SaveTask) },
                onCloseClick = navigator::navigateUp
            )
        },
    ) { padding ->
        CompositionLocalProvider(LocalContentColor provides Black) {
            TaskySurface(modifier = Modifier.padding(padding)) {
                Spacer(modifier = Modifier.size(32.dp))
                AgendaBadge(text = stringResource(id = R.string.task), badgeColor = Green)
                Spacer(modifier = Modifier.size(32.dp))
                EventTitle(
                    title = state.task.title,
                    onEditTitle = {
                        navigator.navigate(EditTitleScreenDestination(startingTitle = state.task.title))
                    },
                    isEditing = state.isEditing
                )
                Spacer(modifier = Modifier.size(24.dp))
                TaskyDivider()
                Spacer(modifier = Modifier.size(16.dp))
                AgendaDescription(
                    text = state.task.description,
                    onEditDescription = {
                        navigator.navigate(EditDescriptionScreenDestination(startingDescription = state.task.description))
                    },
                    isEditing = state.isEditing
                )
                Spacer(modifier = Modifier.size(20.dp))
                TaskyDivider()
                TaskyTimeSection(
                    time = state.task.dateTime,
                    isEditing = state.isEditing,
                    onTimeSelected = { onEvent(TaskScreenEvent.OnUpdateTime(it)) },
                    onDateSelected = { onEvent(TaskScreenEvent.OnUpdateDate(it)) }
                )
                TaskyDivider()
                RemindBeforeSection(
                    canEdit = true,
                    remindAtTime = state.task.remindAtTime,
                    onChangeRemindAtTime = { newRemindAtTime ->
                        onEvent(TaskScreenEvent.OnChangeRemindAtTime(newRemindAtTime))
                    }
                )
                TaskyDivider()
                Spacer(modifier = Modifier.weight(1f))
                TaskyDivider()
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = Gray),
                        onClick = { onEvent(TaskScreenEvent.DeleteTask) })
                    {
                        Text(
                            text = stringResource(R.string.delete_task),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(32.dp))
            }
        }
    }
}

@Preview
@Composable
private fun TaskScreenPreview() {
    ScreenSurface {
        TaskScreenContent(
            state = TaskScreenState(
                task = Task.previewData().copy(
                    dateTime = LocalDateTime(
                        LocalDate(2000, 1, 1),
                        LocalTime(15, 15)
                    )
                )
            ),
            onEvent = {},
            navigator = EmptyDestinationsNavigator
        )
    }
}