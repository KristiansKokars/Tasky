package com.kristianskokars.tasky.feature.task.presentation

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.mapBoth
import com.kristianskokars.tasky.core.data.TaskRepository
import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import com.kristianskokars.tasky.feature.task.domain.model.Task
import com.kristianskokars.tasky.lib.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {
    private val _state = MutableStateFlow(TaskScreenState(Task()))
    private val _events = Channel<UIEvent>()

    val state = _state.asStateFlow()
    val events = _events.receiveAsFlow()

    fun onEvent(event: TaskScreenEvent) {
        when (event) {
            TaskScreenEvent.SaveTask -> saveTask()
            TaskScreenEvent.DeleteTask -> deleteTask()
            is TaskScreenEvent.OnDescriptionChanged -> onDescriptionChanged(event.newDescription)
            is TaskScreenEvent.OnTitleChanged -> onTitleChanged(event.newTitle)
            is TaskScreenEvent.OnUpdateTime -> onUpdateTime(event.newTime)
            is TaskScreenEvent.OnUpdateDate -> onUpdateDate(event.newDate)
            is TaskScreenEvent.OnChangeRemindAtTime -> onChangeRemindAtTime(event.newRemindAtTime)
        }
    }

    private fun saveTask() {
        launch {
            _state.update { it.copy(isSaving = true) }
            repository.saveTask(_state.value.task).mapBoth(
                success = {
                    _events.send(UIEvent.SavedSuccessfully)
                },
                failure = {
                    _events.send(UIEvent.ErrorSaving)
                }
            )
            _state.update { it.copy(isSaving = false) }
        }
    }

    private fun deleteTask() {
        launch {
            repository.deleteTask(_state.value.task.id).mapBoth(
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
            state.copy(task = state.task.copy(description = newDescription))
        }
    }

    private fun onTitleChanged(newTitle: String) {
        _state.update { state ->
            state.copy(task = state.task.copy(title = newTitle))
        }
    }

    private fun onUpdateTime(newTime: LocalTime) {
        _state.update { state ->
            val newDateTime = newTime.atDate(state.task.dateTime.date)
            state.copy(task = state.task.copy(dateTime = newDateTime))
        }
    }

    private fun onUpdateDate(newDate: LocalDate) {
        _state.update { state ->
            val newDateTime = newDate.atTime(state.task.dateTime.time)
            state.copy(task = state.task.copy(dateTime = newDateTime))
        }
    }

    private fun onChangeRemindAtTime(newRemindAtTime: RemindAtTime) {
        _state.update { state ->
            state.copy(task = state.task.copy(remindAtTime = newRemindAtTime))
        }
    }

    sealed class UIEvent {
        data object SavedSuccessfully : UIEvent()
        data object ErrorSaving : UIEvent()
        data object DeletedSuccessfully : UIEvent()
        data object ErrorDeleting : UIEvent()
    }
}
