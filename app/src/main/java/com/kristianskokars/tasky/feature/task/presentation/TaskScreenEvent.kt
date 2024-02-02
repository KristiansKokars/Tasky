package com.kristianskokars.tasky.feature.task.presentation

import com.kristianskokars.tasky.core.domain.model.RemindAtTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class TaskScreenEvent {
    data object SaveTask : TaskScreenEvent()
    data object DeleteTask : TaskScreenEvent()
    data class OnUpdateTime(val newTime: LocalTime) : TaskScreenEvent()
    data class OnUpdateDate(val newDate: LocalDate) : TaskScreenEvent()
    data class OnChangeRemindAtTime(val newRemindAtTime: RemindAtTime) : TaskScreenEvent()
    data class OnTitleChanged(val newTitle: String) : TaskScreenEvent()
    data class OnDescriptionChanged(val newDescription: String) : TaskScreenEvent()
}
