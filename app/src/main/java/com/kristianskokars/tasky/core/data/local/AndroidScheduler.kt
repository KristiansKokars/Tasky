package com.kristianskokars.tasky.core.data.local

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.domain.Scheduler
import com.kristianskokars.tasky.lib.toLocalDateTime
import timber.log.Timber
import java.io.Serializable

class AndroidScheduler(
    private val context: Context,
    private val alarmManager: AlarmManager
) : Scheduler {
    override fun scheduleExactAlarmAt(millis: Long, id: String, extras: Map<String, Any>) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            Intent(context, ReminderBroadcastReceiver::class.java).apply {
                putExtra(DeepLinks.Type.ID, id)
                for ((key, value) in extras.entries) {
                    putExtra(key, value as Serializable)
                }
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent)
        Timber.d("Alarm scheduled for $id at ${millis.toLocalDateTime()} ($millis)")
    }

    override fun cancelAlarm(id: String) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            Intent(context, ReminderBroadcastReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Timber.d("Alarm cancelled for $id")
    }
}
