package com.kristianskokars.tasky.feature.reminder.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import com.kristianskokars.tasky.feature.navArgs
import com.kristianskokars.tasky.feature.reminder.data.ReminderRepository
import com.kristianskokars.tasky.lib.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val navArgs = savedStateHandle.navArgs<ReminderScreenNavArgs>()
    private val _state = MutableStateFlow(ReminderScreenState(isEditing = navArgs.isCreatingNewReminder))
    val state = _state.asStateFlow()

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

    private fun saveReminder() {
        launch {
            repository.saveReminder(_state.value.reminder)
        }
    }

    private fun deleteReminder() {
        launch {
            repository.deleteReminder(_state.value.reminder.id)
        }
    }

    private fun onDescriptionChanged(newDescription: String) {
        _state.update { state ->
            state.copy(reminder = state.reminder.copy(description = newDescription))
        }
    }

    private fun onTitleChanged(newTitle: String) {
        _state.update { state ->
            state.copy(reminder = state.reminder.copy(title = newTitle))
        }
    }

    private fun onUpdateTime(newTime: LocalTime) {
        _state.update { state ->
            val newDateTime = newTime.atDate(state.reminder.dateTime.date)
            state.copy(reminder = state.reminder.copy(dateTime = newDateTime))
        }
    }

    private fun onUpdateDate(newDate: LocalDate) {
        _state.update { state ->
            val newDateTime = newDate.atTime(state.reminder.dateTime.time)
            state.copy(reminder = state.reminder.copy(dateTime = newDateTime))
        }
    }

    private fun onChangeRemindAtTime(newRemindAtTime: RemindAtTime) {
        _state.update { state ->
            state.copy(reminder = state.reminder.copy(remindAtTime = newRemindAtTime))
        }
    }

    private fun onBeginEditing() {
        _state.update { it.copy(isEditing = true) }
    }
}