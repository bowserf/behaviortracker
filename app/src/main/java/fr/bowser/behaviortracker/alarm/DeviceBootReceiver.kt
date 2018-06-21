package fr.bowser.behaviortracker.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import fr.bowser.behaviortracker.config.BehaviorTrackerApp

class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(TAG, "BroadcastReceiver")
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i(TAG, "onReceive")
            val alarmStorageManager = BehaviorTrackerApp.getAppComponent(context).provideAlarmStorageManager()
            val alarmTimerManager = BehaviorTrackerApp.getAppComponent(context).provideAlarmTimerManager()

            val alarmTime = alarmStorageManager.getSavedAlarmTime()
            if (alarmTime != null && alarmTime.isActivated) {
                Log.i(TAG, "alarmTime != null")
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