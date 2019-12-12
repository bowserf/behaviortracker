package fr.bowser.behaviortracker.showmodeitem

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer

class ShowModeTimerViewPresenter(
    private val screen: ShowModeTimerViewContract.Screen,
    private val timeManager: TimeManager,
    private val addOn: AddOn
) : ShowModeTimerViewContract.Presenter {

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
            addOn.stopTimer(timer)
        } else {
            addOn.startTimer(timer)
        }
        screen.statusUpdated(timer.isActivate)
    }

    private val timeManagerListener = object : TimeManager.Listener {
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