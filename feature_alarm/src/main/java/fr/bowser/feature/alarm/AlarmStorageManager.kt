package fr.bowser.feature.alarm

interface AlarmStorageManager {

    fun saveAlarmTime(hour: Int, minute: Int, isActivated: Boolean)

    fun disableAlarmTime()

    fun removeAlarmTime()

    fun getSavedAlarmTime(): AlarmTime?
}