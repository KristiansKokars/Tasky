package com.kristianskokars.tasky.feature.event.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyDivider
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGreen
import com.kristianskokars.tasky.feature.agenda.presentation.components.AddVisitorDialog
import com.kristianskokars.tasky.feature.destinations.EditDescriptionScreenDestination
import com.kristianskokars.tasky.feature.destinations.EditTitleScreenDestination
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaBadge
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaDescription
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaTopBar
import com.kristianskokars.tasky.feature.event.presentation.components.EventTitle
import com.kristianskokars.tasky.feature.event.presentation.components.PhotosSection
import com.kristianskokars.tasky.feature.event.presentation.components.RemindBeforeSection
import com.kristianskokars.tasky.feature.event.presentation.components.TaskyTimeSection
import com.kristianskokars.tasky.feature.event.presentation.components.TimeState
import com.kristianskokars.tasky.feature.event.presentation.components.VisitorsSection
import com.kristianskokars.tasky.lib.fillParentWidth
import com.kristianskokars.tasky.lib.formatToLongDate
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

data class EventScreenNavArgs(val isCreatingNewEvent: Boolean = false)

@AppGraph
@Destination(navArgsDelegate = EventScreenNavArgs::class)
@Composable
fun EventScreen(
    viewModel: EventViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    editTitleResultRecipient: ResultRecipient<EditTitleScreenDestination, String>,
    editDescriptionResultRecipient: ResultRecipient<EditDescriptionScreenDestination, String>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    editTitleResultRecipient.onNavResult { titleResult ->
        when (titleResult) {
            NavResult.Canceled -> { /* Ignored */ }
            is NavResult.Value -> viewModel.onEvent(EventScreenEvent.OnTitleChanged(titleResult.value))
        }
    }
    editDescriptionResultRecipient.onNavResult { descriptionResult ->
        when (descriptionResult) {
            NavResult.Canceled -> { /* Ignored */ }
            is NavResult.Value -> viewModel.onEvent(
                EventScreenEvent.OnDescriptionChanged(
                    descriptionResult.value
                )
            )
        }
    }

    val uploadImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri == null) return@rememberLauncherForActivityResult

            viewModel.onEvent(EventScreenEvent.OnAddPhoto(uri))
        }
    )
    val onUploadImage = remember { { uploadImage.launch("image/*")} }

    EventScreenContent(
        onEvent = viewModel::onEvent,
        state = state,
        navigator = navigator,
        onAddPhotoClick = onUploadImage
    )
}

@Composable
private fun EventScreenContent(
    onEvent: (EventScreenEvent) -> Unit,
    state: EventState,
    navigator: DestinationsNavigator,
    onAddPhotoClick: () -> Unit,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AgendaTopBar(
                title = state.currentDate.formatToLongDate(),
                editingTitle = state.currentDate.formatToLongDate(),
                isEditing = state.isEditing,
                onCloseClick = navigator::navigateUp,
                onSaveClick = { onEvent(EventScreenEvent.SaveEdits) },
                onEditClick = { onEvent(EventScreenEvent.BeginEditing) },
            )
        },
    ) { padding ->
        CompositionLocalProvider(LocalContentColor provides Black) {
            AddVisitorDialog(
                isDialogOpen = isDialogOpen,
                isCheckingIfAttendeeExists = state.isCheckingIfAttendeeExists,
                onDismissRequest = { isDialogOpen = false },
                onAddVisitor = {}
            )
            TaskySurface(
                modifier = Modifier
                    .padding(padding)
//                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.size(32.dp))
                AgendaBadge(text = stringResource(id = R.string.event), badgeColor = LightGreen)
                Spacer(modifier = Modifier.size(8.dp))
                EventTitle(
                    modifier = Modifier.fillParentWidth(16.dp),
                    title = state.title,
                    onEditTitle = {
                        navigator.navigate(EditTitleScreenDestination(state.title))
                    },
                    isEditing = state.isEditing
                )
                Spacer(modifier = Modifier.size(8.dp))
                TaskyDivider()
                Spacer(modifier = Modifier.size(16.dp))
                AgendaDescription(
                    modifier = Modifier.fillParentWidth(16.dp),
                    text = state.description ?: "",
                    onEditDescription = {
                        navigator.navigate(EditDescriptionScreenDestination(state.description ?: ""))
                    },
                    isEditing = state.isEditing,
                )
                Spacer(modifier = Modifier.size(32.dp))
                PhotosSection(
                    photoUrls = state.photos,
                    onAddPhotoClick = onAddPhotoClick
                )
                Spacer(modifier = Modifier.size(32.dp))
                TaskyDivider()
                TaskyTimeSection(
                    isEditing = state.isEditing,
                    time = state.fromDateTime,
                    timeState = TimeState.From,
                    onTimeSelected = { onEvent(EventScreenEvent.OnUpdateFromTime(it)) },
                    onDateSelected = { onEvent(EventScreenEvent.OnUpdateFromDate(it)) }
                )
                TaskyDivider()
                TaskyTimeSection(
                    isEditing = state.isEditing,
                    time = state.toDateTime,
                    timeState = TimeState.To,
                    onTimeSelected = { onEvent(EventScreenEvent.OnUpdateToTime(it)) },
                    onDateSelected = { onEvent(EventScreenEvent.OnUpdateToDate(it)) }
                )
                TaskyDivider()
                RemindBeforeSection(
                    isEditing = state.isEditing,
                    remindAtTime = state.remindAtTime,
                    onChangeRemindAtTime = { newRemindAtTime ->
                        onEvent(EventScreenEvent.OnChangeRemindAtTime(newRemindAtTime))
                    }
                )
                TaskyDivider()
                VisitorsSection(
                    isEditing = state.isEditing,
                    onEditVisitors = { isDialogOpen = true },
                    creator = state.creator,
                    attendees = state.attendees
                )
                Spacer(modifier = Modifier.size(44.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = Gray),
                        onClick = { /*TODO*/ })
                    {
                        Text(text = stringResource(R.string.delete_event))
                    }
                }
            }
        }
    }
}

@Preview(heightDp = 2000)
@Composable
private fun EventScreenPreview() {
    ScreenSurface {
        EventScreenContent(onEvent = {}, state = EventState(), onAddPhotoClick = {}, navigator = EmptyDestinationsNavigator)
    }
}
