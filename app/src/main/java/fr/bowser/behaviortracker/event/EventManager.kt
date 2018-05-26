package fr.bowser.behaviortracker.event

interface EventManager {

    fun sendTimerCreateEvent(startNow: Boolean)

    fun sendExclusiveTimerMode(isExclusive: Boolean)

    fun sendNewTimeFixTimerDuration(newTimer: Int)

}