package fr.bowser.feature.alarm.internal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fr.bowser.feature.alarm.AlarmGraph
import fr.bowser.feature.alarm.AlarmStorageManager
import fr.bowser.feature.alarm.AlarmTimerManager

internal class TimedDayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmListenerManager = AlarmGraphInternal.getAlarmListenerManager()
        alarmListenerManager.notifyAlarmTriggered()
        setTimerForNextDay()
    }

    private fun setTimerForNextDay() {
        val alarmStorageManager = AlarmGraph.getAlarmStorageManager()
        val alarmTimerManager = AlarmGraph.getAlarmTimerManager()
        val alarmTime = alarmStorageManager.getSavedAlarmTime()
        if (alarmTime != null) {
            alarmTimerManager.setAlarm(alarmTime.hour, alarmTime.minute, true)
        }
    }
}
