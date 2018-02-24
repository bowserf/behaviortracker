package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.notification.TimerNotificationManager

class HomePresenter(private val view: HomeContract.View,
                    private val timerNotificationManager: TimerNotificationManager)
    : HomeContract.Presenter {

    override fun start() {
        timerNotificationManager.changeNotifOngoing(false)
    }

    override fun stop() {
        timerNotificationManager.changeNotifOngoing(true)
    }
}