package com.kristianskokars.tasky.feature.event.presentation.event

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.presentation.components.LoadingSpinner
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyAlertDialog
import com.kristianskokars.tasky.core.presentation.components.TaskyDivider
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.components.agendacreation.AgendaBadge
import com.kristianskokars.tasky.core.presentation.components.agendacreation.AgendaDescription
import com.kristianskokars.tasky.core.presentation.components.agendacreation.AgendaTitle
import com.kristianskokars.tasky.core.presentation.components.agendacreation.AgendaTopBar
import com.kristianskokars.tasky.core.presentation.components.agendacreation.TaskyTimeSection
import com.kristianskokars.tasky.core.presentation.components.agendacreation.TimeState
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGreen
import com.kristianskokars.tasky.destinations.EditDescriptionScreenDestination
import com.kristianskokars.tasky.destinations.EditTitleScreenDestination
import com.kristianskokars.tasky.destinations.PhotoDetailScreenDestination
import com.kristianskokars.tasky.feature.agenda.presentation.components.AddVisitorDialog
import com.kristianskokars.tasky.feature.event.presentation.components.PhotosSection
import com.kristianskokars.tasky.feature.event.presentation.components.RemindBeforeSection
import com.kristianskokars.tasky.feature.event.presentation.components.visitorsSection
import com.kristianskokars.tasky.feature.event.presentation.photo.DeletePhoto
import com.kristianskokars.tasky.lib.ObserveAsEvents
import com.kristianskokars.tasky.lib.formatToLongDateUppercase
import com.kristianskokars.tasky.lib.randomID
import com.kristianskokars.tasky.lib.showToast
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

data class EventScreenNavArgs(
    val id: String = randomID(),
    val isEditing: Boolean = false,
    val isCreatingNewEvent: Boolean = false
)

@AppGraph
@Destination(
    navArgsDelegate = EventScreenNavArgs::class,
    deepLinks = [
        DeepLink(uriPattern = DeepLinks.eventPattern)
    ]
)
@Composable
fun EventScreen(
    viewModel: EventViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    editTitleResultRecipient: ResultRecipient<EditTitleScreenDestination, String>,
    editDescriptionResultRecipient: ResultRecipient<EditDescriptionScreenDestination, String>,
    photoResultRecipient: ResultRecipient<PhotoDetailScreenDestination, DeletePhoto?>
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
    photoResultRecipient.onNavResult { photoResult ->
        when (photoResult) {
            NavResult.Canceled -> { /* Ignored */ }
            is NavResult.Value -> {
                val photoResultValue = photoResult.value
                if (photoResultValue !is DeletePhoto) return@onNavResult

                viewModel.onEvent(EventScreenEvent.OnRemovePhoto(photoResultValue.photo))
            }
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

    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            EventViewModel.UIEvent.ErrorSaving -> showToast(context, R.string.failed_save_event)
            EventViewModel.UIEvent.SavedSuccessfully -> showToast(context, R.string.save_event)
            EventViewModel.UIEvent.DeletedSuccessfully -> showToast(context, R.string.deleted_event)
            EventViewModel.UIEvent.ErrorDeleting -> showToast(context, R.string.failed_delete_event)
        }
    }

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
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    if (showConfirmDeleteDialog) {
        TaskyAlertDialog(
            title = { Text(text = stringResource(id = R.string.delete_event_alert_dialog_title)) },
            text = { Text(text = stringResource(R.string.confirm_event_delete)) },
            onConfirm = { onEvent(EventScreenEvent.Delete) },
            onDismissRequest = { showConfirmDeleteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            AgendaTopBar(
                title = state.currentDate.formatToLongDateUppercase(),
                editingTitle = state.currentDate.formatToLongDateUppercase(),
                isEditing = state.isEditing,
                onCloseClick = navigator::navigateUp,
                onSaveClick = { onEvent(EventScreenEvent.Save) },
                onEditClick = { onEvent(EventScreenEvent.BeginEditing) },
            )
        },
    ) { padding ->
        CompositionLocalProvider(LocalContentColor provides Black) {
            AddVisitorDialog(
                isDialogOpen = isDialogOpen,
                isCheckingIfAttendeeExists = state.isCheckingIfAttendeeExists,
                onDismissRequest = { isDialogOpen = false },
                onAddVisitor = { onEvent(EventScreenEvent.AddAttendee(it)) }
            )
            TaskySurface(
                modifier = Modifier.padding(padding)
            ) {
                if (state.event == null) {
                    Spacer(modifier = Modifier.size(24.dp))
                    LoadingSpinner()
                    return@TaskySurface
                }

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Spacer(modifier = Modifier.size(24.dp))
                        AgendaBadge(text = stringResource(id = R.string.event), badgeColor = LightGreen)
                        Spacer(modifier = Modifier.size(16.dp))
                        AgendaTitle(
                            title = state.event.title,
                            onEditTitle = {
                                navigator.navigate(EditTitleScreenDestination(state.event.title))
                            },
                            isEditing = state.isEditing
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        TaskyDivider()
                        Spacer(modifier = Modifier.size(8.dp))
                        AgendaDescription(
                            text = state.event.description,
                            onEditDescription = {
                                navigator.navigate(EditDescriptionScreenDestination(state.event.description))
                            },
                            isEditing = state.isEditing,
                        )
                        if (state.event.photos.isNotEmpty() || state.isEditing) {
                            Spacer(modifier = Modifier.size(8.dp))
                            PhotosSection(
                                photos = state.event.photos,
                                isEditing = state.isEditing,
                                onAddPhotoClick = onAddPhotoClick,
                                onPhotoClick = { navigator.navigate(PhotoDetailScreenDestination(photo = it, canDelete = state.isEditing)) }
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                        TaskyDivider()
                        TaskyTimeSection(
                            isEditing = state.isEditing,
                            dateTime = state.event.fromDateTime,
                            timeState = TimeState.From,
                            onTimeSelected = { onEvent(EventScreenEvent.OnUpdateFromTime(it)) },
                            onDateSelected = { onEvent(EventScreenEvent.OnUpdateFromDate(it)) },
                        )
                        TaskyDivider()
                        TaskyTimeSection(
                            isEditing = state.isEditing,
                            dateTime = state.event.toDateTime,
                            timeState = TimeState.To,
                            onTimeSelected = { onEvent(EventScreenEvent.OnUpdateToTime(it)) },
                            onDateSelected = { onEvent(EventScreenEvent.OnUpdateToDate(it)) },
                        )
                        TaskyDivider()
                        RemindBeforeSection(
                            isEditing = state.isEditing,
                            remindAtTime = state.event.remindAtTime,
                            onChangeRemindAtTime = { newRemindAtTime ->
                                onEvent(EventScreenEvent.OnChangeRemindAtTime(newRemindAtTime))
                            }
                        )
                        TaskyDivider()
                    }
                    visitorsSection(
                        isEditing = state.isEditing,
                        onEditVisitors = { isDialogOpen = true },
                        creator = state.event.creator,
                        attendees = state.event.attendees
                    )
                    item {
                        Spacer(modifier = Modifier.size(44.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(
                                colors = ButtonDefaults.textButtonColors(contentColor = Gray),
                                onClick = { showConfirmDeleteDialog = true }
                            )
                            {
                                Text(text = stringResource(R.string.delete_event))
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
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
