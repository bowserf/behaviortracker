package fr.bowser.feature.alarm.internal

object AlarmListenerModule {

    fun createAlarmListener(): AlarmListenerManager {
        return AlarmListenerManagerImpl()
    }
}
