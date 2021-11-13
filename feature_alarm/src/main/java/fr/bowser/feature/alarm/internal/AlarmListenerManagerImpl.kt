package fr.bowser.feature.alarm.internal

internal class AlarmListenerManagerImpl : AlarmListenerManager {

    private val listeners = mutableListOf<AlarmListenerManager.Listener>()

    override fun notifyAlarmTriggered() {
        listeners.forEach { it.onAlarmTriggered() }
    }

    override fun addListener(listener: AlarmListenerManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun removeListener(listener: AlarmListenerManager.Listener) {
        listeners.remove(listener)
    }
}
