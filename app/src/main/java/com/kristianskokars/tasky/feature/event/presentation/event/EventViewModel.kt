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
import com.kristianskokars.tasky.core.domain.model.RemindAtTime
import com.kristianskokars.tasky.feature.event.domain.PhotoConverter
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.lib.asStateFlow
import com.kristianskokars.tasky.lib.launch
import com.kristianskokars.tasky.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    userStore: DataStore<UserSettings>,
    private val photoConverter: PhotoConverter,
    private val repository: EventRepository,
) : ViewModel() {
    private val navArgs = savedStateHandle.navArgs<EventScreenNavArgs>()
    private val _state = MutableStateFlow(
        EventState(
            event = if (navArgs.isCreatingNewEvent) Event() else null,
            isEditing = navArgs.isCreatingNewEvent)
    )
    private val _events = Channel<UIEvent>()

    val state = combine(
        userStore.data,
        _state,
    ) { user, state ->
        if (navArgs.isCreatingNewEvent) {
            if (user.userId == null || user.fullName == null) return@combine state

            val creator = Attendee(userId = user.userId, email = "", fullName = user.fullName)
            state.copy(event = state.event?.copy(creator = creator))
        } else state
    }.asStateFlow(viewModelScope, EventState(isEditing = navArgs.isCreatingNewEvent))
    val events = _events.receiveAsFlow()

    init {
        if (!navArgs.isCreatingNewEvent) fetchEvent(navArgs.id)
    }

    private fun fetchEvent(eventId: String) {
        launch {
            val event = repository.getEvent(eventId).first() ?: return@launch

            _state.update { it.copy(event = event) }
        }
    }

    fun onEvent(event: EventScreenEvent) {
        when (event) {
            EventScreenEvent.Save -> saveEvent()
            EventScreenEvent.Delete -> deleteEvent()
            is EventScreenEvent.OnDescriptionChanged -> onDescriptionChanged(event.newDescription)
            is EventScreenEvent.OnTitleChanged -> onTitleChanged(event.newTitle)
            is EventScreenEvent.OnAddPhoto -> onAddPhoto(event.newPhoto)
            is EventScreenEvent.OnChangeRemindAtTime -> onChangeRemindAtTime(event.newRemindAtTime)
            is EventScreenEvent.OnUpdateFromDate -> onUpdateFromDate(event.newFromDate)
            is EventScreenEvent.OnUpdateFromTime -> onUpdateFromTime(event.newFromTime)
            is EventScreenEvent.OnUpdateToDate -> onUpdateToDate(event.newToDate)
            is EventScreenEvent.OnUpdateToTime -> onUpdateToTime(event.newToTime)
            is EventScreenEvent.AddAttendee -> onAddAttendee(event.addAttendeeEmail)
            EventScreenEvent.BeginEditing -> onBeginEditing()
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
                        photos = event.photos.toMutableList().apply { add(compressedPhoto) }
                    )
                )
            }
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
            val attendees = _state.value.event?.attendees ?: return@launch
            // TODO: add case to inform this happened
            if (attendees.find { it.email == newAttendeeEmail } != null) return@launch

            _state.update { it.copy(isCheckingIfAttendeeExists = true, errorAttendeeDoesNotExist = false) }
            val response = repository.getAttendee(newAttendeeEmail)
            response.mapBoth(
                success = { attendee ->
                    _state.update { state ->
                        val event = state.event ?: return@update state
                        // TODO: add case to inform this happened
                        if (event.creator?.userId == attendee.userId) return@launch

                        val currentAttendees = event.attendees.toMutableList().apply { add(attendee) }
                        state.copy(
                            isCheckingIfAttendeeExists = false,
                            event = event.copy(attendees = currentAttendees)
                        )
                    }
                },
                failure = {
                    _state.update { it.copy(errorAttendeeDoesNotExist = true) }
                }
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

    sealed class UIEvent {
        data object SavedSuccessfully : UIEvent()
        data object ErrorSaving : UIEvent()
        data object DeletedSuccessfully : UIEvent()
        data object ErrorDeleting : UIEvent()
    }
}
