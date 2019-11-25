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
            ACTION_CONTINUE_POMODORO -> {
                continuePomodoro(context!!)
            }
        }
    }

    private fun continuePomodoro(context: Context) {
        val pomodoroManager = BehaviorTrackerApp.getAppComponent(context).providePomodoroManager()
        pomodoroManager.resume()
    }

    private fun notificationDismiss(context: Context) {
        val notifManager =
            BehaviorTrackerApp.getAppComponent(context).provideTimerNotificationManager()
        notifManager.dismissNotification()
    }

    private fun pauseTimer(context: Context) {
        val notifManager =
            BehaviorTrackerApp.getAppComponent(context).provideTimerNotificationManager()
        val timeManager = BehaviorTrackerApp.getAppComponent(context).provideTimeManager()
        notifManager.timer?.let {
            timeManager.stopTimer(notifManager.timer!!)
        }
    }

    private fun startTimer(context: Context) {
        val notifManager =
            BehaviorTrackerApp.getAppComponent(context).provideTimerNotificationManager()
        val timeManager = BehaviorTrackerApp.getAppComponent(context).provideTimeManager()
        notifManager.timer?.let {
            timeManager.startTimer(notifManager.timer!!)
        }
    }

    companion object {

        private const val ACTION_PLAY = "timer_receiver.action.play"

        private const val ACTION_PAUSE = "timer_receiver.action.pause"

        private const val ACTION_CONTINUE_POMODORO = "timer_receiver.action.continue_pomodoro"

        private const val ACTION_NOTIFICATION_DISMISS = "timer_receiver.action.notification_dismiss"

        fun getPlayPendingIntent(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_PLAY)
        }

        fun getPausePendingIntent(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_PAUSE)
        }

        fun getDeletePendingIntent(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_NOTIFICATION_DISMISS)
        }

        fun getContinuePomodoroPendingIntent(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_CONTINUE_POMODORO)
        }

        private fun getPendingIntent(context: Context, action: String): PendingIntent {
            val intent = Intent(context, TimerReceiver::class.java)
            intent.action = action
            return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}