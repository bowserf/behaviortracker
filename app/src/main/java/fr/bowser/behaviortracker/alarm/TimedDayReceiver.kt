package fr.bowser.behaviortracker.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fr.bowser.behaviortracker.config.BehaviorTrackerApp

class TimedDayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        AlarmNotification.displayAlarmNotif(context)

        // set a new timer
        val alarmStorageManager = BehaviorTrackerApp.getAppComponent(context).provideAlarmStorageManager()
        val alarmTimerManager = BehaviorTrackerApp.getAppComponent(context).provideAlarmTimerManager()

        val alarmTime = alarmStorageManager.getSavedAlarmTime()
        if (alarmTime != null) {
            alarmTimerManager.setAlarm(alarmTime.hour, alarmTime.minute, true)
        }
    }

}