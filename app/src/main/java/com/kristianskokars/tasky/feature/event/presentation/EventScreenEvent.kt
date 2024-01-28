package com.kristianskokars.tasky.feature.event.presentation

import android.net.Uri

sealed class EventScreenEvent {
    data class OnTitleChanged(val newTitle: String) : EventScreenEvent()
    data class OnDescriptionChanged(val newDescription: String) : EventScreenEvent()
    data class OnAddPhoto(val newPhoto: Uri) : EventScreenEvent()

    data object BeginEditing : EventScreenEvent()
    data object SaveEdits : EventScreenEvent()
}