package fr.bowser.behaviortracker.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fr.bowser.behaviortracker.config.BehaviorTrackerApp

class TimerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        when (action) {
            ACTION_PLAY -> {
                startTimer(context!!)
            }
            ACTION_PAUSE -> {
                pauseTimer(context!!)
            }
            ACTION_NOTIFICATION_DISMISS -> {
                notificationDismiss(context!!)
            }
        }
    }

    private fun notificationDismiss(context: Context) {
        val notifManager = BehaviorTrackerApp.getAppComponent(context).provideTimerNotificationManager()
        notifManager.notificationDismiss()
    }

    private fun pauseTimer(context: Context) {
        val notifManager = BehaviorTrackerApp.getAppComponent(context).provideTimerNotificationManager()
        val timerListManager = BehaviorTrackerApp.getAppComponent(context).provideTimerListManager()
        notifManager.timerState?.let {
            timerListManager.updateTimerState(notifManager.timerState!!, false)
            notifManager.pauseTimerNotif()
        }
    }

    private fun startTimer(context: Context) {
        val notifManager = BehaviorTrackerApp.getAppComponent(context).provideTimerNotificationManager()
        val timerListManager = BehaviorTrackerApp.getAppComponent(context).provideTimerListManager()
        notifManager.timerState?.let {
            timerListManager.updateTimerState(notifManager.timerState!!, true)
            notifManager.resumeTimerNotif()
        }
    }

    companion object {

        private val TAG = "TimerReceiver"

        private val ACTION_PLAY = "timer_receiver.action.play"

        private val ACTION_PAUSE = "timer_receiver.action.pause"

        private val ACTION_NOTIFICATION_DISMISS = "timer_receiver.action.notification_dismiss"

        fun getPlayPendingIntent(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_PLAY)
        }

        fun getPausePendingIntent(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_PAUSE)
        }

        fun getDeletePendingIntent(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_NOTIFICATION_DISMISS)
        }

        private fun getPendingIntent(context: Context, action: String): PendingIntent {
            val intent = Intent(context, TimerReceiver::class.java)
            intent.action = action
            return PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
        }

    }

}