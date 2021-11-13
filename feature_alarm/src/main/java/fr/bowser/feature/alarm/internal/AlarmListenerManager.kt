package fr.bowser.feature.alarm.internal

interface AlarmListenerManager {

    fun notifyAlarmTriggered()

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {
        fun onAlarmTriggered()
    }
}
