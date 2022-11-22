package fr.bowser.behaviortracker.event

interface EventManager {

    fun sendTimerCreateEvent(startNow: Boolean)

    fun sendSetAlarmEvent(enable: Boolean)

    fun sendDisplayAlarmNotificationEvent()

    fun sendAlarmNotificationClickedEvent()

    fun sendClickBuyInAppEvent(sku: String)
}
