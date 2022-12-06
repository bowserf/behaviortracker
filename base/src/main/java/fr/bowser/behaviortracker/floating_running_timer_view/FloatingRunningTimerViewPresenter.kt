package fr.bowser.behaviortracker.floating_running_timer_view

import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.TimeConverter

class FloatingRunningTimerViewPresenter(
    private val screen: FloatingRunningTimerViewContract.Screen,
    private val timeManager: TimerManager,
    private val timerListManager: TimerListManager
) : FloatingRunningTimerViewContract.Presenter {

    private var currentTimer: Timer? = null

    private val timeManagerListener = createTimeManagerListener()
    private val timerListManagerListener = createTimerListManagerListener()

    override fun onStart() {
        currentTimer = timeManager.getStartedTimer()
        updateVisibility()
        updateTimerInfo()
        updateTimerTime()
        timeManager.addListener(timeManagerListener)
        timerListManager.addListener(timerListManagerListener)
    }

    override fun onStop() {
        timeManager.removeListener(timeManagerListener)
        timerListManager.removeListener(timerListManagerListener)
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

    private fun updateTimerTime() {
        val timer = currentTimer ?: return
        val newTime = timer.time.toLong()
        val timeToDisplay = TimeConverter.convertSecondsToHumanTime(newTime)
        screen.updateTime(timeToDisplay)
    }

    private fun updateTimerInfo() {
        val timer = currentTimer ?: return
        screen.updateTimer(timer.name)
        screen.changeState(timer.isActivate)
    }

    private fun createTimeManagerListener() = object : TimerManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            currentTimer = updatedTimer
            updateVisibility()
            updateTimerInfo()
            updateTimerTime()
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            updateTimerTime()
        }
    }

    private fun createTimerListManagerListener() = object : TimerListManager.Listener {
        override fun onTimerRemoved(removedTimer: Timer) {
            if (removedTimer != currentTimer) {
                return
            }
            currentTimer = null
            updateVisibility()
        }

        override fun onTimerAdded(updatedTimer: Timer) {
            // nothing to do
        }

        override fun onTimerRenamed(updatedTimer: Timer) {
            if (updatedTimer != currentTimer) {
                return
            }
            updateTimerInfo()
        }
    }
}
