package fr.bowser.behaviortracker.show_mode_item_view

import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer.Timer

class ShowModeItemViewPresenter(
    private val screen: ShowModeItemViewContract.Screen,
    private val timeManager: TimerManager
) : ShowModeItemViewContract.Presenter {

    private lateinit var timer: Timer

    override fun onStart() {
        timeManager.addListener(timeManagerListener)
    }

    override fun onStop() {
        timeManager.removeListener(timeManagerListener)
    }

    override fun setTimer(timer: Timer) {
        this.timer = timer

        screen.statusUpdated(timer.isActivate)
    }

    override fun onClickView() {
        if (timer.isActivate) {
            timeManager.stopTimer()
        } else {
            timeManager.startTimer(timer)
        }
        screen.statusUpdated(timer.isActivate)
    }

    private val timeManagerListener = object : TimerManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                screen.statusUpdated(updatedTimer.isActivate)
            }
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                screen.timerUpdated(timer.time.toLong())
            }
        }
    }

    interface AddOn {
        fun startTimer(timer: Timer)
        fun stopTimer(timer: Timer)
    }
}
