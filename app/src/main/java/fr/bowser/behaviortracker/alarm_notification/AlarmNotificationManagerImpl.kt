package fr.bowser.behaviortracker.alarm_notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.home_activity.HomeActivity

class AlarmNotificationManagerImpl(
    private val context: Context,
    private val eventManager: EventManager,
) : AlarmNotificationManager {

    override fun displayNotification() {
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE,
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            generateNotificationChannel(notificationManager)
        }

        val homeIntent = Intent(context, HomeActivity::class.java)
        homeIntent.action = ACTION_ALARM_NOTIFICATION_CLICKED
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(
            context,
            0,
            homeIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            },
        )

        val notificationBuilder = NotificationCompat.Builder(
            context,
            context.resources.getString(R.string.alarm_timer_notif_channel_id),
        )
            .setContentTitle(context.resources.getString(R.string.alarm_timer_title))
            .setContentText(context.resources.getString(R.string.alarm_timer_content))
            .setSmallIcon(R.drawable.alarm_notification_timer)
            .setAutoCancel(true)
            .setLights(Color.YELLOW, DURATION_LIGHT_ON, DURATION_LIGHT_OFF)
            .setContentIntent(resultPendingIntent)

        notificationManager.notify(
            ALARM_NOTIFICATION_ID,
            notificationBuilder.build(),
        )

        eventManager.sendDisplayAlarmNotificationEvent()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun generateNotificationChannel(notificationManager: NotificationManager) {
        val channelId = context.getString(R.string.alarm_timer_notif_channel_id)
        val channelName = context.getString(R.string.alarm_timer_notif_channel_name)
        val channelDescription = context.getString(R.string.alarm_timer_notif_channel_description)
        val channelImportance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            channelImportance,
        )
        notificationChannel.description = channelDescription
        notificationChannel.enableVibration(false)
        notificationChannel.enableLights(true)
        notificationChannel.setShowBadge(false)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        const val ACTION_ALARM_NOTIFICATION_CLICKED = "alarm_notification.action.alarm_clicked"

        private const val ALARM_NOTIFICATION_ID = 786

        private const val DURATION_LIGHT_ON = 500
        private const val DURATION_LIGHT_OFF = 3000
    }
}
