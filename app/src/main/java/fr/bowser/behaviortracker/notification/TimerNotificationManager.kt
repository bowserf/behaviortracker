package fr.bowser.behaviortracker.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.support.v4.app.NotificationCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.home.HomeActivity
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerState
import fr.bowser.behaviortracker.utils.TimeConverter


class TimerNotificationManager(private val context: Context,
                               private val timeManager: TimeManager)
    : TimeManager.UpdateTimerCallback {

    private val notificationManager: NotificationManager
            = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var timerNotificationBuilder: NotificationCompat.Builder? = null

    private var isNotificationDisplayed = false

    var timerState: TimerState? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            generateNotificationChannel(context.resources)
        }
    }

    override fun timeUpdated() {
        timerState?.let { updateTimerNotif() }
    }

    fun displayTimerNotif(timerState: TimerState) {
        this.timerState = timerState

        isNotificationDisplayed = true

        // action when click on notification
        val homeIntent = Intent(context, HomeActivity::class.java)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(
                context, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        timerNotificationBuilder = NotificationCompat.Builder(context, context.resources.getString(R.string.timer_notif_channel_id))
                .setContentTitle(timerState.timer.name)
                .setContentText(TimeConverter.convertSecondsToHumanTime(
                        timerState.timer.currentTime))
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(resultPendingIntent)
                .setDeleteIntent(TimerReceiver.getDeletePendingIntent(context))
                .addAction(R.drawable.ic_pause,
                        context.resources.getString(R.string.timer_notif_pause),
                        TimerReceiver.getPausePendingIntent(context))

        notificationManager.notify(
                TIMER_NOTIFICATION_ID,
                timerNotificationBuilder?.build())

        timeManager.registerUpdateTimerCallback(this)
    }

    fun resumeTimerNotif() {
        if(!isNotificationDisplayed){
            return
        }

        timeManager.registerUpdateTimerCallback(this)

        timerNotificationBuilder?.mActions?.clear()
        timerNotificationBuilder?.addAction(R.drawable.ic_pause,
                context.resources.getString(R.string.timer_notif_pause),
                TimerReceiver.getPausePendingIntent(context))

        timerNotificationBuilder?.let {
            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder?.build())
        }
    }

    fun pauseTimerNotif() {
        if(!isNotificationDisplayed){
            return
        }
        timerNotificationBuilder?.mActions?.clear()
        timerNotificationBuilder?.addAction(R.drawable.ic_play,
                context.resources.getString(R.string.timer_notif_start),
                TimerReceiver.getPlayPendingIntent(context))

        timeManager.unregisterUpdateTimerCallback(this)

        timerNotificationBuilder?.let {
            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder?.build())
        }
    }

    fun destroyNotif(removedTimer: TimerState) {
        if(timerState == removedTimer) {
            isNotificationDisplayed = false

            timerState = null
            timeManager.unregisterUpdateTimerCallback(this)
            notificationManager.cancel(TIMER_NOTIFICATION_ID)
        }
    }

    fun renameTimerNotif(updatedTimer : TimerState) {
        if(timerState == updatedTimer) {
            timerNotificationBuilder?.setContentTitle(timerState!!.timer.name)
            timerNotificationBuilder?.let {
                notificationManager.notify(
                        TIMER_NOTIFICATION_ID,
                        timerNotificationBuilder?.build())
            }
        }
    }

    fun updateTimerNotif(updatedTimer : TimerState){
        if(timerState == updatedTimer) {
            updateTimerNotif()
        }
    }

    fun notificationDismiss() {
        isNotificationDisplayed = false
        timeManager.unregisterUpdateTimerCallback(this)
    }

    private fun updateTimerNotif() {
        timerNotificationBuilder?.setContentText(
                TimeConverter.convertSecondsToHumanTime(timerState!!.timer.currentTime))
        timerNotificationBuilder?.let {
            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder?.build())
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun generateNotificationChannel(res: Resources) {
        val channelId = res.getString(R.string.timer_notif_channel_id)
        val channelName = res.getString(R.string.timer_notif_channel_name)
        val channelDescription = res.getString(R.string.timer_notif_channel_description)
        val channelImportance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                channelImportance)
        notificationChannel.description = channelDescription
        notificationChannel.enableVibration(false)
        notificationChannel.enableLights(false)
        notificationChannel.setShowBadge(false)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {

        private val TIMER_NOTIFICATION_ID = 42

    }

}