package fr.bowser.behaviortracker.alarm

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.home.HomeActivity

object AlarmNotification {

    const val ACTION_ALARM_NOTIFICATION_CLICKED = "alarm_notification.action.alarm_clicked"

    private const val ALARM_NOTIFICATION_ID = 786

    private const val DURATION_LIGHT_ON = 500
    private const val DURATION_LIGHT_OFF = 3000

    fun displayAlarmNotif(context: Context) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            generateNotificationChannel(notificationManager, context.resources)
        }

        val homeIntent = Intent(context, HomeActivity::class.java)
        homeIntent.action = ACTION_ALARM_NOTIFICATION_CLICKED
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(
            context, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notifBuilder = NotificationCompat.Builder(
            context,
            context.resources.getString(R.string.alarm_timer_notif_channel_id)
        )
            .setContentTitle(context.resources.getString(R.string.alarm_timer_title))
            .setContentText(context.resources.getString(R.string.alarm_timer_content))
            .setSmallIcon(R.drawable.ic_timer)
            .setAutoCancel(true)
            .setLights(Color.YELLOW, DURATION_LIGHT_ON, DURATION_LIGHT_OFF)
            .setContentIntent(resultPendingIntent)

        notificationManager.notify(
            ALARM_NOTIFICATION_ID,
            notifBuilder.build()
        )

        val eventManager = BehaviorTrackerApp
            .getAppComponent(context)
            .provideEventManager()
        eventManager.sendDisplayAlarmNotificationEvent()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun generateNotificationChannel(notifManager: NotificationManager, res: Resources) {
        val channelId = res.getString(R.string.alarm_timer_notif_channel_id)
        val channelName = res.getString(R.string.alarm_timer_notif_channel_name)
        val channelDescription = res.getString(R.string.alarm_timer_notif_channel_description)
        val channelImportance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            channelImportance
        )
        notificationChannel.description = channelDescription
        notificationChannel.enableVibration(false)
        notificationChannel.enableLights(true)
        notificationChannel.setShowBadge(false)
        notifManager.createNotificationChannel(notificationChannel)
    }
}