package fr.bowser.behaviortracker.timer

import fr.bowser.behaviortracker.home.TimerContract
import fr.bowser.behaviortracker.model.Timer

class TimerPresenter(val view: TimerContract.View) : TimerContract.Presenter, TimerActionListener{

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