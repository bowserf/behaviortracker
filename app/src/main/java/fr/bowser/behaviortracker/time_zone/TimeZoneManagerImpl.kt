package fr.bowser.behaviortracker.time_zone

class TimeZoneManagerImpl : TimeZoneManager {

    private val listeners = mutableListOf<TimeZoneManager.Listener>()

    override fun notifyTimeZoneUpdated() {
        listeners.forEach { it.onTimeZoneChanged() }
    }

    override fun addListener(listener: TimeZoneManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun removeListener(listener: TimeZoneManager.Listener) {
        listeners.remove(listener)
    }
}
