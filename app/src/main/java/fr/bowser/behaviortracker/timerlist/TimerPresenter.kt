package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.TimerListManager

class TimerPresenter(private val view: TimerContract.View,
                     private val timerListManager: TimerListManager)
    : TimerContract.Presenter {

    override fun start() {
        view.displayTimerList(timerListManager.timers)
    }

    override fun onClickAddTimer() {
        view.displayCreateTimerView()
    }

}