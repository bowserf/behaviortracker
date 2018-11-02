package fr.bowser.behaviortracker.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.core.app.NotificationCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.KillAppDetection
import fr.bowser.behaviortracker.home.HomeActivity
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.TimeConverter


class TimerNotificationManagerImpl(private val context: Context,
                                   timeManager: TimeManager,
                                   timerListManager: TimerListManager)
    : TimerNotificationManager, TimeManager.Listener, TimerListManager.Listener {

    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var timerNotificationBuilder: NotificationCompat.Builder? = null

    private var isNotificationDisplayed = false

    private var isAppInBackground = false

    override var timer: Timer? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            generateNotificationChannel(context.resources)
        }

        timeManager.addListener(this)
        timerListManager.addListener(this)
    }

    override fun changeOngoingState(isAppInBackground: Boolean) {
        if (!isNotificationDisplayed) {
            return
        }
        this.isAppInBackground = isAppInBackground
        timerNotificationBuilder!!.let {
            it.setOngoing(isAppInBackground)
            notificationManager.notify(TIMER_NOTIFICATION_ID, it.build())
        }
    }

    override fun dismissNotification(killProcess: Boolean) {
        if (!isNotificationDisplayed) {
            return
        }

        timer = null
        isNotificationDisplayed = false

        if (killProcess) {
            stopKillAppDectectionService()
        }

        notificationManager.cancel(TIMER_NOTIFICATION_ID)
    }

    override fun onTimerStateChanged(updatedTimer: Timer) {
        if (updatedTimer.isActivate) {
            displayTimerNotif(updatedTimer)
        } else {
            pauseTimerNotif(updatedTimer)
        }
    }

    override fun onTimerTimeChanged(updatedTimer: Timer) {
        if (timer == updatedTimer) {
            updateTimeNotif()
        }
    }

    override fun onTimerRemoved(removedTimer: Timer) {
        if (timer == removedTimer) {
            dismissNotification()
        }
    }

    override fun onTimerAdded(updatedTimer: Timer) {
        // if timer is directly activate, display it in the notification
        if (updatedTimer.isActivate) {
            displayTimerNotif(updatedTimer)
        }
    }

    override fun onTimerRenamed(updatedTimer: Timer) {
        if (timer == updatedTimer) {
            renameTimerNotif(updatedTimer)
        }
    }

    private fun displayTimerNotif(modifiedTimer: Timer) {
        if (timer == modifiedTimer) {
            resumeTimerNotif(modifiedTimer)
        } else {
            this.timer = modifiedTimer

            isNotificationDisplayed = true

            // action when click on notification
            val homeIntent = Intent(context, HomeActivity::class.java)
            homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val resultPendingIntent = PendingIntent.getActivity(
                    context, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            timerNotificationBuilder = NotificationCompat.Builder(
                    context,
                    context.resources.getString(R.string.timer_notif_channel_id))
                    .setContentTitle(modifiedTimer.name)
                    .setContentText(TimeConverter.convertSecondsToHumanTime(
                            modifiedTimer.time.toLong()))
                    .setSmallIcon(R.drawable.ic_timer)
                    .setContentIntent(resultPendingIntent)
                    .setDeleteIntent(TimerReceiver.getDeletePendingIntent(context))
                    .addAction(R.drawable.ic_pause,
                            context.resources.getString(R.string.timer_notif_pause),
                            TimerReceiver.getPausePendingIntent(context))

            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder!!.build())

            startKillAppDectectionService()
        }
    }

    private fun renameTimerNotif(updatedTimer: Timer) {
        if (timer == updatedTimer) {
            timerNotificationBuilder!!.let {
                it.setContentTitle(timer!!.name)
                notificationManager.notify(TIMER_NOTIFICATION_ID, it.build())
            }
        }
    }

    private fun resumeTimerNotif(modifiedTimer: Timer) {
        if (!isNotificationDisplayed || timer != modifiedTimer) {
            return
        }

        timerNotificationBuilder!!.let {
            it.setOngoing(isAppInBackground)

            it.mActions?.clear()
            it.addAction(R.drawable.ic_pause,
                    context.resources.getString(R.string.timer_notif_pause),
                    TimerReceiver.getPausePendingIntent(context))

            notificationManager.notify(TIMER_NOTIFICATION_ID, it.build())
        }
    }

    private fun pauseTimerNotif(modifiedTimer: Timer) {
        if (!isNotificationDisplayed || timer != modifiedTimer) {
            return
        }

        timerNotificationBuilder!!.let {
            // allow to remove notification when timer is not running
            it.setOngoing(false)

            it.mActions?.clear()
            it.addAction(R.drawable.ic_play,
                    context.resources.getString(R.string.timer_notif_start),
                    TimerReceiver.getPlayPendingIntent(context))

            notificationManager.notify(TIMER_NOTIFICATION_ID, it.build())
        }
    }

    private fun updateTimeNotif() {
        timerNotificationBuilder!!.let {
            it.setContentText(
                    TimeConverter.convertSecondsToHumanTime(timer!!.time.toLong()))

            notificationManager.notify(TIMER_NOTIFICATION_ID, it.build())
        }
    }

    private fun startKillAppDectectionService() {
        val serviceIntent = Intent(context, KillAppDetection::class.java)
        context.startService(serviceIntent)
    }

    private fun stopKillAppDectectionService() {
        val serviceIntent = Intent(context, KillAppDetection::class.java)
        context.stopService(serviceIntent)
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

        private const val TIMER_NOTIFICATION_ID = 42

    }

}