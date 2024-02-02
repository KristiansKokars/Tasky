package com.kristianskokars.tasky.feature.reminder.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyDivider
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.feature.destinations.EditDescriptionScreenDestination
import com.kristianskokars.tasky.feature.destinations.EditTitleScreenDestination
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaBadge
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaDescription
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaTitle
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaTopBar
import com.kristianskokars.tasky.feature.event.presentation.components.RemindBeforeSection
import com.kristianskokars.tasky.feature.event.presentation.components.TaskyTimeSection
import com.kristianskokars.tasky.lib.fillParentWidth
import com.kristianskokars.tasky.lib.randomID
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.datetime.LocalDate

data class ReminderScreenNavArgs(val id: String = randomID(), val isCreatingNewReminder: Boolean = false)

@AppGraph
@Destination(
    navArgsDelegate = ReminderScreenNavArgs::class,
    deepLinks = [
        DeepLink(uriPattern = DeepLinks.reminderPattern)
    ]
)
@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    editTitleResultRecipient: ResultRecipient<EditTitleScreenDestination, String>,
    editDescriptionResultRecipient: ResultRecipient<EditDescriptionScreenDestination, String>
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    editTitleResultRecipient.onNavResult { titleResult ->
        when (titleResult) {
            NavResult.Canceled -> { /* Ignored */ }
            is NavResult.Value -> viewModel.onEvent(ReminderScreenEvent.OnTitleChanged(titleResult.value))
        }
    }
    editDescriptionResultRecipient.onNavResult { descriptionResult ->
        when (descriptionResult) {
            NavResult.Canceled -> { /* Ignored */ }
            is NavResult.Value -> viewModel.onEvent(
                ReminderScreenEvent.OnDescriptionChanged(
                    descriptionResult.value
                )
            )
        }
    }

    ReminderScreenContent(
        state = state,
        isDateAllowed = viewModel::isDateAllowed,
        onEvent = viewModel::onEvent,
        navigator = navigator
    )
}

@Composable
private fun ReminderScreenContent(
    state: ReminderScreenState,
    isDateAllowed: (LocalDate) -> Boolean,
    onEvent: (ReminderScreenEvent) -> Unit,
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            AgendaTopBar(
                isEditing = state.isEditing,
                onCloseClick = navigator::navigateUp,
                onSaveClick = { onEvent(ReminderScreenEvent.Save) },
                onEditClick = { onEvent(ReminderScreenEvent.BeginEditing) },
                editingTitle = stringResource(R.string.edit_reminder).uppercase()
            )
        },
    ) { padding ->
        CompositionLocalProvider(LocalContentColor provides Black) {
            TaskySurface(modifier = Modifier.padding(padding)) {
                Spacer(modifier = Modifier.size(32.dp))
                AgendaBadge(
                    text = stringResource(id = R.string.reminder),
                    badgeColor = LightGray,
                    badgeModifier = Modifier.border(1.dp, Gray, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.size(32.dp))
                AgendaTitle(
                    modifier = Modifier.fillParentWidth(16.dp),
                    title = state.reminder.title,
                    isEditing = state.isEditing,
                    onEditTitle = {
                        navigator.navigate(EditTitleScreenDestination(state.reminder.title))
                    }
                )
                Spacer(modifier = Modifier.size(24.dp))
                TaskyDivider()
                Spacer(modifier = Modifier.size(16.dp))
                AgendaDescription(
                    text = state.reminder.description,
                    isEditing = state.isEditing,
                    onEditDescription = {
                        navigator.navigate(EditDescriptionScreenDestination(state.reminder.description))
                    }
                )
                Spacer(modifier = Modifier.size(20.dp))
                TaskyDivider()
                TaskyTimeSection(
                    time = state.reminder.dateTime,
                    isEditing = state.isEditing,
                    onTimeSelected = { onEvent(ReminderScreenEvent.OnUpdateTime(it)) },
                    onDateSelected = { onEvent(ReminderScreenEvent.OnUpdateDate(it)) },
                    allowedTimeRange = state.allowedTimeRange,
                    allowedDateValidator = isDateAllowed
                )
                TaskyDivider()
                RemindBeforeSection(
                    isEditing = state.isEditing,
                    remindAtTime = state.reminder.remindAtTime,
                    onChangeRemindAtTime = { newRemindAtTime ->
                        onEvent(ReminderScreenEvent.OnChangeRemindAtTime(newRemindAtTime))
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
                        onClick = { onEvent(ReminderScreenEvent.Delete) })
                    {
                        Text(
                            text = stringResource(R.string.delete_reminder),
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
private fun ReminderScreenPreview() {
    ScreenSurface {
        ReminderScreenContent(
            state = ReminderScreenState(),
            isDateAllowed = { true },
            onEvent = {},
            navigator = EmptyDestinationsNavigator
        )
    }
}