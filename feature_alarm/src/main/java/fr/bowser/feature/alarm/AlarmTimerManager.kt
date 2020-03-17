package fr.bowser.feature.alarm

interface AlarmTimerManager {

    fun setAlarm(hour: Int, minute: Int, delayOneDay: Boolean = false)

    fun removeAlarm()

    fun getSavedAlarmTimer(): AlarmTime?

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {
        fun onAlarmTriggered()
    }
}