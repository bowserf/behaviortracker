package fr.bowser.behaviortracker.showmodeitem

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer

class ShowModeTimerViewPresenter(
    val view: ShowModeTimerViewContract.View,
    val timeManager: TimeManager
) : ShowModeTimerViewContract.Presenter {

    private lateinit var timer: Timer

    override fun start() {
        timeManager.addListener(timeManagerListener)
    }

    override fun stop() {
        timeManager.removeListener(timeManagerListener)
    }

    override fun setTimer(timer: Timer) {
        this.timer = timer

        view.statusUpdated(timer.isActivate)
    }

    override fun onClickView() {
        if (timer.isActivate) {
            timeManager.stopTimer(timer)
        } else {
            timeManager.startTimer(timer)
        }
        view.statusUpdated(timer.isActivate)
    }

    private val timeManagerListener = object : TimeManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                view.statusUpdated(updatedTimer.isActivate)
            }
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                view.timerUpdated(timer.time.toLong())
            }
        }
    }
}