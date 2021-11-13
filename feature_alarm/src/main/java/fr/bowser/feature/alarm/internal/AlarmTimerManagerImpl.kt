package fr.bowser.feature.alarm.internal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import fr.bowser.feature.alarm.AlarmStorageManager
import fr.bowser.feature.alarm.AlarmTime
import fr.bowser.feature.alarm.AlarmTimerManager
import java.util.Calendar

internal class AlarmTimerManagerImpl(
    private val context: Context,
    private val alarmStorageManager: AlarmStorageManager,
    private val alarmListenerManager: AlarmListenerManager
) : AlarmTimerManager {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val listeners = mutableListOf<AlarmTimerManager.Listener>()

    private var alarmListener = createAlarmListener()

    override fun setAlarm(hour: Int, minute: Int, delayOneDay: Boolean) {
        val intent = Intent(context, TimedDayReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        if (delayOneDay) {
            calendar.add(Calendar.DATE, 1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }

        changeDeviceBootReceiverStatus(context, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)

        alarmStorageManager.saveAlarmTime(hour, minute, true)
    }

    override fun removeAlarm() {
        val intent = Intent(context, TimedDayReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.cancel(alarmIntent)

        changeDeviceBootReceiverStatus(context, PackageManager.COMPONENT_ENABLED_STATE_DISABLED)

        alarmStorageManager.disableAlarmTime()
    }

    override fun getSavedAlarmTimer(): AlarmTime? {
        return alarmStorageManager.getSavedAlarmTime()
    }

    override fun addListener(listener: AlarmTimerManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }

        if (listeners.isEmpty()) {
            alarmListenerManager.addListener(alarmListener)
        }

        listeners.add(listener)
    }

    override fun removeListener(listener: AlarmTimerManager.Listener) {
        listeners.remove(listener)
    }

    private fun notifyAlarmTriggered() {
        listeners.forEach { it.onAlarmTriggered() }
    }

    private fun changeDeviceBootReceiverStatus(context: Context, state: Int) {
        val receiver = ComponentName(context, DeviceBootReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
            receiver,
            state,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun createAlarmListener(): AlarmListenerManager.Listener {
        return object : AlarmListenerManager.Listener {
            override fun onAlarmTriggered() {
                notifyAlarmTriggered()
            }
        }
    }
}
