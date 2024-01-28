package com.kristianskokars.tasky.feature.event.presentation

sealed class EventScreenEvent {
    data class OnTitleChanged(val newTitle: String) : EventScreenEvent()
    data class OnDescriptionChanged(val newDescription: String) : EventScreenEvent()

    data object BeginEditing : EventScreenEvent()
    data object SaveEdits : EventScreenEvent()
}