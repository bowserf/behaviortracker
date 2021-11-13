package fr.bowser.feature.alarm.internal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import fr.bowser.feature.alarm.AlarmGraph

internal class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val alarmStorageManager = AlarmGraph.getAlarmStorageManager()
            val alarmTimerManager = AlarmGraph.getAlarmTimerManager()

            val alarmTime = alarmStorageManager.getSavedAlarmTime()
            if (alarmTime != null && alarmTime.isActivated) {
                Log.i(TAG, "alarmTime.hour " + alarmTime.hour)
                Log.i(TAG, "alarmTime.minute " + alarmTime.minute)
                alarmTimerManager.setAlarm(alarmTime.hour, alarmTime.minute)
            }
        }
    }

    companion object {
        const val TAG = "DeviceBootReceiver"
    }
}
