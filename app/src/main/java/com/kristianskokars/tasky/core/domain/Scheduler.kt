package com.kristianskokars.tasky.core.domain

interface Scheduler {
    fun scheduleExactAlarmAt(millis: Long, id: String, extras: Map<String, Any> = emptyMap())
}
