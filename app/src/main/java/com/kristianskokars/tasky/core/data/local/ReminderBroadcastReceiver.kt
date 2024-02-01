package com.kristianskokars.tasky.core.data.local

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.kristianskokars.tasky.MainActivity
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.domain.Constants
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.domain.toDeepLinkType
import com.kristianskokars.tasky.lib.formatToDateWithHHMM
import com.kristianskokars.tasky.lib.toLocalDateTime

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        val id = intent.extras?.getString(DeepLinks.Type.ID) ?: return
        val type = intent.extras?.getString(DeepLinks.Type.KEY)?.toDeepLinkType() ?: return
        val name = intent.extras?.getString(DeepLinks.Extra.NAME.toString()) ?: return
        val timeInMillis = intent.extras?.getLong(DeepLinks.Extra.TIME.toString()) ?: return

        val notificationManager = NotificationManagerCompat.from(context)
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            DeepLinks.item(id, type).toUri(),
            context,
            MainActivity::class.java
        )
        val deepLinkPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val contentTitle = context.getString(R.string.reminder_notification_title, name)
        val contentText = context.getString(R.string.reminder_notification_description, name, timeInMillis.toLocalDateTime().formatToDateWithHHMM())
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(deepLinkPendingIntent)
            .build()

        notificationManager.notify(id.hashCode(), notification)
    }
}
