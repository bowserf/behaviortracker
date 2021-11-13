package fr.bowser.feature.alarm.internal

internal object AlarmListenerModule {

    fun createAlarmListener(): AlarmListenerManager {
        return AlarmListenerManagerImpl()
    }
}
