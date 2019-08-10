package fr.bowser.behaviortracker.alarm

interface AlarmTimerManager {

    fun setAlarm(hour: Int, minute: Int, delayOneDay: Boolean = false)

    fun removeAlarm()

    fun getSavedAlarmTimer(): AlarmTime?
}