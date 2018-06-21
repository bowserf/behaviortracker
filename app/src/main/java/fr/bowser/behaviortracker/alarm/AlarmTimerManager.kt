package fr.bowser.behaviortracker.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import java.util.*

class AlarmTimerManager(private val context: Context,
                        private val alarmStorageManager: AlarmStorageManager) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(hour: Int, minute: Int, delayOneDay: Boolean = false) {
        val intent = Intent(context, TimedDayReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        if(delayOneDay) {
            calendar.add(Calendar.DATE, 1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
        }

        changeDeviceBootReceiverStatus(context, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)

        alarmStorageManager.saveAlarmTime(hour, minute, true)
    }

    fun removeAlarm() {
        val intent = Intent(context, TimedDayReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.cancel(alarmIntent)

        changeDeviceBootReceiverStatus(context, PackageManager.COMPONENT_ENABLED_STATE_DISABLED)

        alarmStorageManager.disableAlarmTime()
    }

    fun getSavedAlarmTimer(): AlarmStorageManager.AlarmTime? {
        return alarmStorageManager.getSavedAlarmTime()
    }

    private fun changeDeviceBootReceiverStatus(context: Context, state: Int) {
        val receiver = ComponentName(context, DeviceBootReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(receiver,
                state,
                PackageManager.DONT_KILL_APP)
    }

}