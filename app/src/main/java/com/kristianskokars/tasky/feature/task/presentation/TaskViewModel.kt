package com.kristianskokars.tasky.feature.task.presentation

import androidx.lifecycle.ViewModel
import com.kristianskokars.tasky.core.data.TaskRepository
import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
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
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {
    private val _state = MutableStateFlow(TaskScreenState(Task()))
    val state = _state.asStateFlow()

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
            repository.saveTask(_state.value.task)
        }
    }

    private fun deleteTask() {
        launch {
            repository.deleteTask(_state.value.task.id)
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
}