package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState

class TimerPresenter(private val view: TimerContract.View,
                     private val timerListManager: TimerListManager)
    : TimerContract.Presenter, TimerListManager.TimerCallback {

    override fun start() {
        timerListManager.registerTimerCallback(this)
        val timers = timerListManager.timersState
        view.displayTimerList(timers)
    }

    override fun stop() {
        timerListManager.unregisterTimerCallback(this)
    }

    override fun onTimerRemoved(timer: TimerState, position:Int) {
        view.onTimerRemoved(timer, position)
    }

    override fun onTimerAdded(timer: TimerState, position: Int) {
        view.onTimerAdded(timer, position)
    }

    override fun onClickAddTimer() {
        view.displayCreateTimerView()
    }

    override fun onTimerStateChanged(timer: TimerState, position: Int) {
        // nothing to do
    }
}