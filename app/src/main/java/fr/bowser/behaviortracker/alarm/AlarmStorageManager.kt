package fr.bowser.behaviortracker.alarm

interface AlarmStorageManager {

    fun saveAlarmTime(hour: Int, minute: Int, isActivated: Boolean)

    fun disableAlarmTime()

    fun removeAlarmTime()

    fun getSavedAlarmTime(): AlarmTime?

}