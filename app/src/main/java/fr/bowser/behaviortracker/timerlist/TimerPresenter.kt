package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class TimerPresenter(private val view: TimerContract.View,
                     private val timerListManager: TimerListManager,
                     private val timerNotificationManager: TimerNotificationManager)
    : TimerContract.Presenter, TimerListManager.TimerCallback {

    override fun start() {
        timerListManager.registerTimerCallback(this)
        val timers = timerListManager.getTimerList()
        view.displayTimerList(timers)

        if (timers.isEmpty()) {
            view.displayEmptyListView()
        } else {
            view.displayListView()
        }
    }

    override fun stop() {
        timerListManager.unregisterTimerCallback(this)
    }

    override fun onClickAddTimer() {
        view.displayCreateTimerView()
    }

    override fun onTimerRemoved(updatedTimer: Timer) {
        if (timerListManager.getTimerList().isEmpty()) {
            view.displayEmptyListView()
        }
        view.onTimerRemoved(updatedTimer)
    }

    override fun onTimerAdded(updatedTimer: Timer) {
        if (!timerListManager.getTimerList().isEmpty()) {
            view.displayListView()
        }
        // if timer is directly activate, display it in the notification
        if (updatedTimer.isActivate) {
            timerNotificationManager.displayTimerNotif(updatedTimer)
        }
        view.onTimerAdded(updatedTimer)
    }

    override fun onTimerRenamed(updatedTimer: Timer) {
        // nothing to do
    }
}