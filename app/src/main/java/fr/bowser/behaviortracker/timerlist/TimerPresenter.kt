package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class TimerPresenter(private val view: TimerContract.View,
                     private val timerListManager: TimerListManager)
    : TimerContract.Presenter, TimerListManager.TimerCallback {

    override fun start() {
        timerListManager.registerTimerCallback(this)
        view.displayTimerList(timerListManager.timers)
    }

    override fun stop() {
        timerListManager.unregisterTimerCallback(this)
    }

    override fun onTimerRemoved(timer: Timer) {
        view.onTimerRemoved(timer)
    }

    override fun onTimerAdded(timer: Timer) {
        view.onTimerAdded(timer)
    }

    override fun onClickAddTimer() {
        view.displayCreateTimerView()
    }

}