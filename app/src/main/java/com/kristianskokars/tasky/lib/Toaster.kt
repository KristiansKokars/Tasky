package com.kristianskokars.tasky.lib

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Toaster @Inject constructor() {
    private val _messages = MutableSharedFlow<Message>(replay = 0, extraBufferCapacity = 1)
    val messages = _messages.asSharedFlow()

    suspend fun show(message: Message) {
        _messages.emit(message)
    }

    data class Message(val text: String)
}
