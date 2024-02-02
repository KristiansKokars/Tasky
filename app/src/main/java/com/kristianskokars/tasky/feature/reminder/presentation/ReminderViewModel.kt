package com.kristianskokars.tasky.feature.reminder.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.mapBoth
import com.kristianskokars.tasky.core.data.ReminderRepository
import com.kristianskokars.tasky.core.domain.model.RemindAtTime
import com.kristianskokars.tasky.core.domain.model.Reminder
import com.kristianskokars.tasky.feature.navArgs
import com.kristianskokars.tasky.lib.allTimesOfDay
import com.kristianskokars.tasky.lib.currentDate
import com.kristianskokars.tasky.lib.currentTime
import com.kristianskokars.tasky.lib.launch
import com.kristianskokars.tasky.lib.localTimeMax
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository,
    private val clock: Clock,
    private val timeZone: TimeZone,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val navArgs = savedStateHandle.navArgs<ReminderScreenNavArgs>()
    private val _state = MutableStateFlow(
        ReminderScreenState(
            reminder = if (navArgs.isCreatingNewReminder) Reminder() else null,
            isEditing = navArgs.isEditing,
            allowedTimeRange = clock.currentTime(timeZone) .. localTimeMax()
        )
    )
    private val _events = Channel<UIEvent>()

    val state = _state.asStateFlow()
    val events = _events.receiveAsFlow()

    init {
        if (!navArgs.isCreatingNewReminder) fetchReminder(navArgs.id)
    }

    private fun fetchReminder(reminderId: String) {
        launch {
            val reminder = repository.getReminder(reminderId).first() ?: return@launch

            _state.update { it.copy(reminder = reminder) }
        }
    }

    fun onEvent(event: ReminderScreenEvent) {
        when (event) {
            ReminderScreenEvent.Save -> saveReminder()
            ReminderScreenEvent.Delete -> deleteReminder()
            is ReminderScreenEvent.OnDescriptionChanged -> onDescriptionChanged(event.newDescription)
            is ReminderScreenEvent.OnTitleChanged -> onTitleChanged(event.newTitle)
            is ReminderScreenEvent.OnUpdateTime -> onUpdateTime(event.newTime)
            is ReminderScreenEvent.OnUpdateDate -> onUpdateDate(event.newDate)
            is ReminderScreenEvent.OnChangeRemindAtTime -> onChangeRemindAtTime(event.newRemindAtTime)
            ReminderScreenEvent.BeginEditing -> onBeginEditing()
        }
    }

    fun isDateAllowed(localDate: LocalDate): Boolean {
        return localDate >= clock.currentDate(timeZone)
    }

    private fun saveReminder() {
        launch {
            val reminder = _state.value.reminder ?: return@launch

            _state.update { it.copy(isSaving = true) }
            repository.saveReminder(reminder).mapBoth(
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

    private fun deleteReminder() {
        launch {
            val reminder = _state.value.reminder ?: return@launch

            repository.deleteReminder(reminder.id).mapBoth(
                success = {
                    _events.send(UIEvent.DeletedSuccessfully)
                },
                failure = {
                    _events.send(UIEvent.ErrorDeleting)
                }
            )
        }
    }

    private fun onDescriptionChanged(newDescription: String) {
        _state.update { state ->
            if (state.reminder == null) return@update state

            state.copy(reminder = state.reminder.copy(description = newDescription))
        }
    }

    private fun onTitleChanged(newTitle: String) {
        _state.update { state ->
            if (state.reminder == null) return@update state

            state.copy(reminder = state.reminder.copy(title = newTitle))
        }
    }

    private fun onUpdateTime(newTime: LocalTime) {
        _state.update { state ->
            if (state.reminder == null) return@update state

            val newDateTime = newTime.atDate(state.reminder.dateTime.date)
            state.copy(reminder = state.reminder.copy(dateTime = newDateTime))
        }
    }

    private fun onUpdateDate(newDate: LocalDate) {
        _state.update { state ->
            if (state.reminder == null) return@update state

            if (newDate == clock.currentDate(timeZone)) {
                val currentTime = clock.currentTime(timeZone)
                val newDateTime = if (currentTime > state.reminder.dateTime.time) {
                    newDate.atTime(currentTime)
                } else newDate.atTime(state.reminder.dateTime.time)

                state.copy(
                    reminder = state.reminder.copy(dateTime = newDateTime),
                    allowedTimeRange = clock.currentTime(timeZone) .. localTimeMax(),
                )
            } else {
                val newDateTime = newDate.atTime(state.reminder.dateTime.time)
                state.copy(
                    reminder = state.reminder.copy(dateTime = newDateTime),
                    allowedTimeRange = allTimesOfDay(),
                )
            }
        }
    }

    private fun onChangeRemindAtTime(newRemindAtTime: RemindAtTime) {
        _state.update { state ->
            if (state.reminder == null) return@update state

            state.copy(reminder = state.reminder.copy(remindAtTime = newRemindAtTime))
        }
    }

    private fun onBeginEditing() {
        _state.update { it.copy(isEditing = true) }
    }

    sealed class UIEvent {
        data object SavedSuccessfully : UIEvent()
        data object ErrorSaving : UIEvent()
        data object DeletedSuccessfully : UIEvent()
        data object ErrorDeleting : UIEvent()
    }
}
