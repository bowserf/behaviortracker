package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.notification.TimerNotificationManager

class HomePresenter(
    private val timerNotificationManager: TimerNotificationManager,
    private val eventManager: EventManager
) : HomeContract.Presenter {

    override fun start() {
        timerNotificationManager.changeOngoingState(false)
    }

    override fun stop() {
        timerNotificationManager.changeOngoingState(true)
    }

    override fun onAlarmNotificationClicked() {
        eventManager.sendAlarmNotificationClickedEvent()
    }
}