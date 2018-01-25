package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.home.TimerContract
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager

class TimerPresenter(private val view: TimerContract.View,
                     private val timerManager: TimerManager)
    : TimerContract.Presenter,
        TimerActionListener {

    override fun onClickAddTimer() {
        view.displayCreateTimerView()
    }

    override fun onTimerStateChange(timer: Timer) {

    }

    override fun onClickIncreaseTime(timer: Timer) {

    }

    override fun onClickDecreaseTime(timer: Timer) {

    }
}