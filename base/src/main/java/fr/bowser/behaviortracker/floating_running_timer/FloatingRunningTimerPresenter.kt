package fr.bowser.behaviortracker.floating_running_timer

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.TimeConverter

class FloatingRunningTimerPresenter(
    private val screen: FloatingRunningTimerContract.Screen,
    private val timeManager: TimeManager
) : FloatingRunningTimerContract.Presenter {

    private var currentTimer: Timer? = null

    private val timeManagerListener = createTimeManagerListener()

    override fun onStart() {
        currentTimer = timeManager.getStartedTimer()
        updateVisibility()
        timeManager.addListener(timeManagerListener)
    }

    override fun onStop() {
        timeManager.removeListener(timeManagerListener)
    }

    override fun onClickUpdateTime() {
        val currentTimer = currentTimer ?: throw IllegalStateException("No timer is attached.")
        screen.displayUpdateTimer(currentTimer.id)
    }

    override fun onClickPlayPause() {
        val currentTimer = currentTimer ?: throw IllegalStateException("No timer is attached.")
        if (currentTimer.isActivate) {
            timeManager.stopTimer()
        } else {
            timeManager.startTimer(currentTimer)
        }
    }

    private fun updateVisibility() {
        screen.displayView(currentTimer != null)
    }

    private fun updateTimerTime(timer: Timer) {
        val newTime = timer.time.toLong()
        val timeToDisplay = TimeConverter.convertSecondsToHumanTime(newTime)
        screen.updateTime(timeToDisplay)
    }

    private fun createTimeManagerListener() = object : TimeManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            currentTimer = updatedTimer
            updateVisibility()
            screen.updateTimer(updatedTimer.name)
            screen.changeState(updatedTimer.isActivate)
            updateTimerTime(updatedTimer)
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            updateTimerTime(updatedTimer)
        }
    }
}
