package fr.bowser.behaviortracker.timer_service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import fr.bowser.behaviortracker.config.BehaviorTrackerApp

class TimerServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
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
        TimerService.dismissNotification(context)
    }

    private fun pauseTimer(context: Context) {
        val timeManager = BehaviorTrackerApp.getAppComponent(context).provideTimeManager()
        timeManager.stopTimer()
    }

    private fun startTimer(context: Context) {
        val timeManager = BehaviorTrackerApp.getAppComponent(context).provideTimeManager()
        val timerRepository = BehaviorTrackerApp.getAppComponent(context).provideTimerRepositoryManager()
        val startedTimer = timerRepository.getTimerList().maxByOrNull { it.lastUpdateTimestamp }!!
        timeManager.startTimer(startedTimer)
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
            val intent = Intent(context, TimerServiceReceiver::class.java)
            intent.action = action
            return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )
        }
    }
}
