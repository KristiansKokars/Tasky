package com.kristianskokars.tasky.feature.event.presentation.event

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.kristianskokars.tasky.core.data.EventRepository
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.core.domain.model.Event
import com.kristianskokars.tasky.core.domain.model.Photo
import com.kristianskokars.tasky.core.domain.model.RemindAtTime
import com.kristianskokars.tasky.feature.event.domain.PhotoConverter
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.feature.event.domain.model.AttendeeStatusFilter
import com.kristianskokars.tasky.lib.asStateFlow
import com.kristianskokars.tasky.lib.launch
import com.kristianskokars.tasky.lib.randomID
import com.kristianskokars.tasky.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userStore: DataStore<UserSettings>,
    private val photoConverter: PhotoConverter,
    private val repository: EventRepository,
) : ViewModel() {
    private val navArgs = savedStateHandle.navArgs<EventScreenNavArgs>()
    private val _state = MutableStateFlow(EventState(isEditing = navArgs.isEditing))
    private val _events = Channel<UIEvent>()

    val state = _state.map {  state ->
        val currentEvent = state.event ?: return@map state

        val (goingAttendees, notGoingAttendees) = currentEvent.attendees.partition { it.isGoing }
        state.copy(goingAttendees = goingAttendees, notGoingAttendees = notGoingAttendees)
    }.asStateFlow(viewModelScope, EventState(isEditing = navArgs.isEditing))
    val events = _events.receiveAsFlow()

    init {
        if (navArgs.isCreatingNewEvent) {
            createNewEvent()
        } else {
            fetchEvent(navArgs.id)
        }
    }

    private fun createNewEvent() {
        launch {
            val currentUser = userStore.data.first()
            val currentUserId = currentUser.userId ?: return@launch
            val currentUserEmail = currentUser.email ?: return@launch
            val currentUserFullName = currentUser.fullName ?: return@launch

            _state.update { state ->
                if (state.event != null) return@launch

                val eventId = randomID()
                state.copy(
                    event = Event(
                        id = eventId,
                        creatorUserId = currentUserId,
                        attendees = listOf(
                            Attendee(
                                userId = currentUserId,
                                email = currentUserEmail,
                                fullName = currentUserFullName,
                                eventId = eventId,
                                isGoing = true,
                            )
                        ),
                        isUserEventCreator = true
                    ),
                    isCurrentUserGoing = true
                )
            }
        }
    }

    private fun fetchEvent(eventId: String) {
        launch {
            val event = repository.getEvent(eventId).first() ?: return@launch
            val currentUserId = userStore.data.first().userId ?: return@launch

            _state.update { state ->
                state.copy(
                    event = event,
                    isCurrentUserGoing =
                        event.isUserEventCreator ||
                        event.attendees.find { it.userId == currentUserId }?.isGoing ?: false
                )
            }
        }
    }

    fun onEvent(event: EventScreenEvent) {
        when (event) {
            EventScreenEvent.Save -> saveEvent()
            EventScreenEvent.Delete -> deleteEvent()
            is EventScreenEvent.OnDescriptionChanged -> onDescriptionChanged(event.newDescription)
            is EventScreenEvent.OnTitleChanged -> onTitleChanged(event.newTitle)
            is EventScreenEvent.OnAddPhoto -> onAddPhoto(event.newPhoto)
            is EventScreenEvent.OnRemovePhoto -> onRemovePhoto(event.photoToRemove)
            is EventScreenEvent.OnChangeRemindAtTime -> onChangeRemindAtTime(event.newRemindAtTime)
            is EventScreenEvent.OnUpdateFromDate -> onUpdateFromDate(event.newFromDate)
            is EventScreenEvent.OnUpdateFromTime -> onUpdateFromTime(event.newFromTime)
            is EventScreenEvent.OnUpdateToDate -> onUpdateToDate(event.newToDate)
            is EventScreenEvent.OnUpdateToTime -> onUpdateToTime(event.newToTime)
            is EventScreenEvent.AddAttendee -> onAddAttendee(event.addAttendeeEmail)
            is EventScreenEvent.RemoveAttendee -> onRemoveAttendee(event.attendeeToRemove)
            is EventScreenEvent.SwitchStatusFilter -> onSwitchAttendeeFilter(event.newFilter)
            EventScreenEvent.BeginEditing -> onBeginEditing()
            EventScreenEvent.JoinEventAsAttendee -> joinEventAsAttendee()
            EventScreenEvent.LeaveEventAsAttendee -> leaveEventAsAttendee()
        }
    }

    private fun onBeginEditing() {
        _state.update { it.copy(isEditing = true) }
    }

    private fun saveEvent() {
        launch {
            val event = _state.value.event ?: return@launch

            _state.update { it.copy(isSaving = true) }
            repository.saveEvent(event).mapBoth(
                success = {
                    _events.send(UIEvent.SavedSuccessfully)
                    _state.update { it.copy(isSaving = false, isEditing = false) }
                },
                failure = {
                    _events.send(UIEvent.ErrorSaving)
                    _state.update { it.copy(isSaving = false) }
                }
            )
        }
    }

    private fun onDescriptionChanged(newDescription: String) {
        _state.update { state ->
            val event = state.event ?: return@update state

            state.copy(event = event.copy(description = newDescription))
        }
    }

    private fun onTitleChanged(newTitle: String) {
        _state.update { state ->
            val event = state.event ?: return@update state

            state.copy(event = event.copy(title = newTitle))
        }
    }

    private fun onAddPhoto(newPhoto: Uri) {
        launch {
            val compressedPhoto = photoConverter.compressPhoto(newPhoto) ?: return@launch
            _state.update { state ->
                val event = state.event ?: return@update state

                state.copy(
                    event = event.copy(
                        photos = event.photos.toMutableList().apply {
                            add(Photo(isLocal = true, key = "photo${size}", url = compressedPhoto.toString()))
                        }
                    )
                )
            }
        }
    }

    private fun onRemovePhoto(photoToRemove: Photo) {
        _state.update { state ->
            val event = state.event ?: return@update state
            val photo = event.photos.find { it.key == photoToRemove.key } ?: return@update state

            state.copy(event = event.copy(photos = event.photos.toMutableList().apply { remove(photo) }))
        }
    }

    private fun onChangeRemindAtTime(newRemindAtTime: RemindAtTime) {
        _state.update { state ->
            val event = state.event ?: return@update state

            state.copy(event = event.copy(remindAtTime = newRemindAtTime) )
        }
    }

    private fun onUpdateFromDate(newFromDate: LocalDate) {
        _state.update { state ->
            val event = state.event ?: return@update state

            val newDateTime = newFromDate.atTime(event.fromDateTime.time)
            state.copy(event = event.copy(fromDateTime = newDateTime))
        }
    }

    private fun onUpdateFromTime(newFromTime: LocalTime) {
        _state.update { state ->
            val event = state.event ?: return@update state

            val newDateTime = newFromTime.atDate(event.fromDateTime.date)
            state.copy(event = event.copy(fromDateTime = newDateTime))
        }
    }

    private fun onUpdateToDate(newToDate: LocalDate) {
        _state.update { state ->
            val event = state.event ?: return@update state

            val newDateTime = newToDate.atTime(event.toDateTime.time)
            state.copy(event = event.copy(toDateTime = newDateTime))
        }
    }

    private fun onUpdateToTime(newToTime: LocalTime) {
        _state.update { state ->
            val event = state.event ?: return@update state

            val newDateTime = newToTime.atDate(event.toDateTime.date)
            state.copy(event = event.copy(toDateTime = newDateTime))
        }
    }

    private fun onAddAttendee(newAttendeeEmail: String) {
        launch {
            val currentEvent = _state.value.event ?: return@launch

            val attendees = currentEvent.attendees
            if (attendees.find { it.email == newAttendeeEmail } != null) {
                _events.send(UIEvent.AttendeeNotFound(newAttendeeEmail))
                return@launch
            }

            _state.update { it.copy(isCheckingIfAttendeeExists = true) }
            val response = repository.getAttendee(newAttendeeEmail, currentEvent.id)
            response.mapBoth(
                success = { attendee ->
                    _state.update { state ->
                        val event = state.event ?: return@update state
                        // TODO: add case to inform this happened
                        if (event.creatorUserId == attendee.userId) return@launch

                        val currentAttendees = event.attendees.toMutableList().apply { add(attendee) }
                        state.copy(
                            isCheckingIfAttendeeExists = false,
                            event = event.copy(attendees = currentAttendees)
                        )
                    }
                    _events.send(UIEvent.AttendeeAddedSuccessfuly(attendee))
                },
                failure = {
                    _state.update { it.copy(isCheckingIfAttendeeExists = false) }
                    _events.send(UIEvent.FailedToAddAttendee(newAttendeeEmail))
                }
            )
        }
    }

    private fun onRemoveAttendee(attendeeToRemove: Attendee) {
        _state.update { state ->
            val currentEvent = state.event ?: return@update state

            state.copy(
                event = currentEvent.copy(
                    attendees = currentEvent.attendees.toMutableList().apply { remove(attendeeToRemove) }
                )
            )
        }
    }

    private fun deleteEvent() {
        launch {
            val event = _state.value.event ?: return@launch

            repository.deleteEvent(event.id).mapBoth(
                success = { _events.send(UIEvent.DeletedSuccessfully) },
                failure = { _events.send(UIEvent.ErrorDeleting) }
            )
        }
    }

    private fun onSwitchAttendeeFilter(newAttendeeFilter: AttendeeStatusFilter) {
        _state.update { state ->
            state.copy(selectedStatusFilter = newAttendeeFilter)
        }
    }

    private fun joinEventAsAttendee() {
        changeAttendeeIsGoingStatus(isGoing = true)
    }

    private fun leaveEventAsAttendee() {
        changeAttendeeIsGoingStatus(isGoing = false)
    }

    private fun changeAttendeeIsGoingStatus(isGoing: Boolean) {
        launch {
            _state.update { state ->
                val currentEvent = state.event ?: return@update state
                val currentUserId = userStore.data.first().userId ?: return@launch

                val updatedAttendees = currentEvent.attendees.toMutableList().apply {
                    val currentAttendee = find { it.userId == currentUserId } ?: return@apply

                    remove(currentAttendee)
                    add(currentAttendee.copy(isGoing = isGoing))
                }
                state.copy(
                    isCurrentUserGoing = isGoing,
                    event = currentEvent.copy(attendees = updatedAttendees)
                )
            }
        }
        saveEvent()
    }

    sealed class UIEvent {
        data object SavedSuccessfully : UIEvent()
        data object ErrorSaving : UIEvent()
        data object DeletedSuccessfully : UIEvent()
        data object ErrorDeleting : UIEvent()
        data class AttendeeAddedSuccessfuly(val addedAttendee: Attendee) : UIEvent()
        data class FailedToAddAttendee(val failedEmail: String) : UIEvent()
        data class AttendeeNotFound(val failedEmail: String) : UIEvent()
    }
}
