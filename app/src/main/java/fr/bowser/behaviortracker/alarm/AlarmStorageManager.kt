package fr.bowser.behaviortracker.alarm

import android.content.Context
import android.preference.PreferenceManager

class AlarmStorageManager(private val context: Context) {

    fun saveAlarmTime(hour: Int, minute: Int, isActivated: Boolean) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPref.edit()
        edit.putInt(ALARM_HOUR, hour)
        edit.putInt(ALARM_MINUTE, minute)
        edit.putBoolean(ALARM_IS_ACTIVATED, isActivated)
        edit.apply()
    }

    fun disableAlarmTime() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPref.edit()
        edit.remove(ALARM_IS_ACTIVATED)
        edit.apply()
    }

    fun removeAlarmTime() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPref.edit()
        edit.remove(ALARM_HOUR)
        edit.remove(ALARM_MINUTE)
        edit.remove(ALARM_IS_ACTIVATED)
        edit.apply()
    }

    fun getSavedAlarmTime(): AlarmTime? {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val hour = sharedPref.getInt(ALARM_HOUR, -1)
        val minute = sharedPref.getInt(ALARM_MINUTE, -1)
        val isActivated = sharedPref.getBoolean(ALARM_IS_ACTIVATED, false)
        if (hour == -1 && minute == -1) {
            return null
        }
        return AlarmTime(hour, minute, isActivated)
    }

    data class AlarmTime(val hour: Int, val minute: Int, val isActivated: Boolean)

    companion object {
        const val ALARM_HOUR = "alarm_storage_manager.pref_key.alarm_hour"
        const val ALARM_MINUTE = "alarm_storage_manager.pref_key.alarm_minute"
        const val ALARM_IS_ACTIVATED = "alarm_storage_manager.pref_key.alarm_is_activated"
    }


}