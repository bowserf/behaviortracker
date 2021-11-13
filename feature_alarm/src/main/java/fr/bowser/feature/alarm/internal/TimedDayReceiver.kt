package fr.bowser.feature.alarm.internal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fr.bowser.feature.alarm.AlarmGraph

internal class TimedDayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // set a new timer
        val alarmStorageManager = AlarmGraph.getAlarmStorageManager()
        val alarmTimerManager = AlarmGraph.getAlarmTimerManager()
        val alarmListenerManager = AlarmGraphInternal.getAlarmListenerManager()

        alarmListenerManager.notifyAlarmTriggered()

        val alarmTime = alarmStorageManager.getSavedAlarmTime()
        if (alarmTime != null) {
            alarmTimerManager.setAlarm(alarmTime.hour, alarmTime.minute, true)
        }
    }
}
