package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState

class TimerPresenter(private val view: TimerContract.View,
                     private val timerListManager: TimerListManager)
    : TimerContract.Presenter, TimerListManager.TimerCallback {

    override fun start() {
        timerListManager.registerTimerCallback(this)
        val timers = timerListManager.timers
        val timersState = ArrayList<TimerState>()
        timers.mapTo(timersState) { TimerState(false, it) }
        view.displayTimerList(timersState)
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