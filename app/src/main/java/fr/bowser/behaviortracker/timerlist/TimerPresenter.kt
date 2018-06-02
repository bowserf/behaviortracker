package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class TimerPresenter(private val view: TimerContract.View,
                     private val timerListManager: TimerListManager)
    : TimerContract.Presenter, TimerListManager.TimerCallback {

    override fun init() {
        val timers = timerListManager.getTimerList()
        view.displayTimerList(timers)

        if (timers.isEmpty()) {
            view.displayEmptyListView()
        } else {
            view.displayListView()
        }
    }

    override fun start() {
        timerListManager.registerTimerCallback(this)
    }

    override fun stop() {
        timerListManager.unregisterTimerCallback(this)
    }

    override fun onClickAddTimer() {
        view.displayCreateTimerView()
    }

    override fun onTimerSwiped(timer: Timer) {
        timerListManager.removeTimer(timer)
    }

    override fun onReorderFinished(timerList: List<Timer>) {
        timerListManager.reorderTimerList(timerList)
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
        view.onTimerAdded(updatedTimer)
    }

    override fun onTimerRenamed(updatedTimer: Timer) {
        // nothing to do
    }
}