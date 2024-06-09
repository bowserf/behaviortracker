package fr.bowser.behaviortracker.show_mode_item_view

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager

class ShowModeItemViewPresenter(
    private val screen: ShowModeItemViewContract.Screen,
    private val timerManager: TimerManager
) : ShowModeItemViewContract.Presenter {

    private lateinit var timer: Timer

    override fun onStart() {
        timerManager.addListener(timeManagerListener)
    }

    override fun onStop() {
        timerManager.removeListener(timeManagerListener)
    }

    override fun setTimer(timer: Timer) {
        this.timer = timer
        updateTimerStatus()
    }

    override fun onClickView() {
        if (timerManager.isRunning(timer)) {
            timerManager.stopTimer()
        } else {
            timerManager.startTimer(timer)
        }
        updateTimerStatus()
    }

    private val timeManagerListener = object : TimerManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            if (timer != updatedTimer) {
                return
            }
            updateTimerStatus()
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                screen.timerUpdated(timer.time.toLong())
            }
        }
    }

    private fun updateTimerStatus() {
        screen.statusUpdated(timerManager.isRunning(timer))
    }
}
