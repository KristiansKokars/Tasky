package com.kristianskokars.tasky.feature.event.presentation

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.feature.event.data.EventRepository
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.feature.navArgs
import com.kristianskokars.tasky.lib.asStateFlow
import com.kristianskokars.tasky.lib.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoConverter: PhotoConverter,
    private val repository: EventRepository,
    private val userStore: DataStore<UserSettings>,
    private val timeZone: TimeZone,
) : ViewModel() {
    private val navArgs = savedStateHandle.navArgs<EventScreenNavArgs>()
    private val _state = MutableStateFlow(EventState(isEditing = navArgs.isCreatingNewEvent))

    val state = combine(
        userStore.data,
        _state,
    ) { user, state ->
        if (navArgs.isCreatingNewEvent) {
            // TODO: temporary for getting your own user!
            state.copy(creator = Attendee(userId = user.userId!!, email = "", fullName = user.fullName!!))
        } else state
    }.asStateFlow(viewModelScope, EventState(isEditing = navArgs.isCreatingNewEvent))

    fun onEvent(event: EventScreenEvent) {
        when (event) {
            EventScreenEvent.BeginEditing -> onBeginEditing()
            EventScreenEvent.SaveEdits -> onSaveEdits()
            is EventScreenEvent.OnDescriptionChanged -> onDescriptionChanged(event.newDescription)
            is EventScreenEvent.OnTitleChanged -> onTitleChanged(event.newTitle)
            is EventScreenEvent.OnAddPhoto -> onAddPhoto(event.newPhoto)
            is EventScreenEvent.OnChangeRemindAtTime -> onChangeRemindAtTime(event.newRemindAtTime)
            is EventScreenEvent.OnUpdateFromDate -> onUpdateFromDate(event.newFromDate)
            is EventScreenEvent.OnUpdateFromTime -> onUpdateFromTime(event.newFromTime)
            is EventScreenEvent.OnUpdateToDate -> onUpdateToDate(event.newToDate)
            is EventScreenEvent.OnUpdateToTime -> onUpdateToTime(event.newToTime)
            is EventScreenEvent.OnAddAttendee -> onAddAttendee(event.addAttendeeEmail)
        }
    }

    private fun onBeginEditing() {
        _state.update { it.copy(isEditing = true) }
    }

    private fun onSaveEdits() {
        if (navArgs.isCreatingNewEvent) {
            launch {
                val currentState = _state.value
                val fromDateTime = currentState.fromDateTime.toInstant(timeZone)
                repository.saveEvent(
                    currentState.title,
                    currentState.description ?: "",
                    from = fromDateTime.toEpochMilliseconds(),
                    to = currentState.toDateTime.toInstant(timeZone).toEpochMilliseconds(),
                    remindAt = fromDateTime.minus(currentState.remindAtTime.toDuration()).toEpochMilliseconds(),
                    attendeeIds = emptyList(),
                    photos = currentState.photos
                )
            }
        }
        _state.update { it.copy(isEditing = false) }
    }

    private fun onDescriptionChanged(newDescription: String) {
        _state.update { it.copy(description = newDescription) }
    }

    private fun onTitleChanged(newTitle: String) {
        _state.update { it.copy(title = newTitle) }
    }

    private fun onAddPhoto(newPhoto: Uri) {
        launch {
            val compressedPhoto = photoConverter.compressPhoto(newPhoto) ?: return@launch
            _state.update { it.copy(photos = it.photos.toMutableList().apply { add(compressedPhoto) }) }
        }
    }

    private fun onChangeRemindAtTime(newRemindAtTime: RemindAtTime) {
        _state.update { it.copy(remindAtTime = newRemindAtTime) }
    }

    private fun onUpdateFromDate(newFromDate: LocalDate) {
        _state.update { state ->
            Timber.d("Updated time: $newFromDate")
            val newDateTime = newFromDate.atTime(state.fromDateTime.time)
            state.copy(fromDateTime = newDateTime)
        }
    }

    private fun onUpdateFromTime(newFromTime: LocalTime) {
        _state.update { state ->
            val newDateTime = newFromTime.atDate(state.fromDateTime.date)
            Timber.d("NewDateTime: $newDateTime")
            state.copy(fromDateTime = newDateTime)
        }
    }

    private fun onUpdateToDate(newToDate: LocalDate) {
        _state.update { state ->
            val newDateTime = newToDate.atTime(state.toDateTime.time)
            state.copy(toDateTime = newDateTime)
        }
    }

    private fun onUpdateToTime(newToTime: LocalTime) {
        _state.update { state ->
            val newDateTime = newToTime.atDate(state.toDateTime.date)
            state.copy(toDateTime = newDateTime)
        }
    }

    private fun onAddAttendee(newAttendeeEmail: String) {
        launch {
            _state.update { it.copy(isCheckingIfAttendeeExists = true) }
            val response = repository.getAttendee(newAttendeeEmail)
            _state.update { state ->
                state.copy(
                    isCheckingIfAttendeeExists = false,
                    attendees = if (response != null) {
                        state.attendees.toMutableList().apply { add(response) }
                    } else state.attendees
                )
            }
        }
    }
}