package fr.bowser.feature.alarm.internal

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import fr.bowser.feature.alarm.AlarmGraph

internal class AlarmManagerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                setCurrentAlarm()
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                setCurrentAlarm()
            }
        }
    }

    private fun setCurrentAlarm() {
        val alarmTimerManager = AlarmGraph.getAlarmTimerManager()
        if (!alarmTimerManager.canScheduleAlarm()) {
            return
        }
        val alarmStorageManager = AlarmGraph.getAlarmStorageManager()
        val alarmTime = alarmStorageManager.getSavedAlarmTime()
        if (alarmTime != null && alarmTime.isActivated) {
            Log.i(TAG, "alarmTime.hour " + alarmTime.hour)
            Log.i(TAG, "alarmTime.minute " + alarmTime.minute)

            alarmTimerManager.setAlarm(alarmTime.hour, alarmTime.minute)
        }
    }

    companion object {
        const val TAG = "AlarmManagerReceiver"
    }
}
