package com.kristianskokars.tasky.feature.event.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kristianskokars.tasky.feature.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
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
        }
    }

    private fun onBeginEditing() {
        _state.update { it.copy(isEditing = true) }
    }

    private fun onSaveEdits() {
        _state.update { it.copy(isEditing = false) }
    }

    private fun onDescriptionChanged(newDescription: String) {
        _state.update { it.copy(description = newDescription) }
    }

    private fun onTitleChanged(newTitle: String) {
        _state.update { it.copy(title = newTitle) }
    }
}