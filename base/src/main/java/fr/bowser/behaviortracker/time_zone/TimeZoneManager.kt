package fr.bowser.behaviortracker.time_zone

interface TimeZoneManager {

    fun notifyTimeZoneUpdated()

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onTimeZoneChanged()
    }
}
