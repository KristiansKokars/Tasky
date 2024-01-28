package com.kristianskokars.tasky.feature.event.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kristianskokars.tasky.feature.event.data.EventRepository
import com.kristianskokars.tasky.feature.navArgs
import com.kristianskokars.tasky.lib.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoConverter: PhotoConverter,
    private val repository: EventRepository,
) : ViewModel() {
    private val navArgs = savedStateHandle.navArgs<EventScreenNavArgs>()
    private val _state = MutableStateFlow(EventState(isEditing = navArgs.isCreatingNewEvent))

    val state = _state.asStateFlow()

    fun onEvent(event: EventScreenEvent) {
        when (event) {
            EventScreenEvent.BeginEditing -> onBeginEditing()
            EventScreenEvent.SaveEdits -> onSaveEdits()
            is EventScreenEvent.OnDescriptionChanged -> onDescriptionChanged(event.newDescription)
            is EventScreenEvent.OnTitleChanged -> onTitleChanged(event.newTitle)
            is EventScreenEvent.OnAddPhoto -> onAddPhoto(event.newPhoto)
        }
    }

    private fun onBeginEditing() {
        _state.update { it.copy(isEditing = true) }
    }

    private fun onSaveEdits() {
        if (navArgs.isCreatingNewEvent) {
            launch {
                val currentState = _state.value
                repository.saveEvent(
                    currentState.title,
                    currentState.description ?: "",
                    from = 0L,
                    to = 1000L,
                    remindAt = 500L,
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

}