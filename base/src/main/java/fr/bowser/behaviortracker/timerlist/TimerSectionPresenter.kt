package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class TimerSectionPresenter(
    private val screen: TimerSectionContract.Screen,
    private val timerListManager: TimerListManager,
    private val timeManager: TimeManager
) : TimerSectionContract.Presenter {

    private var isTimersActivated = false

    private val timeManagerListener = createTimeManagerListener()
    private val timerListManagerListener = createTimerListManagerListener()

    override fun onResume() {
        timeManager.addListener(timeManagerListener)
        timerListManager.addListener(timerListManagerListener)

        updateTimerList()
    }

    override fun onPause() {
        timeManager.removeListener(timeManagerListener)
        timerListManager.removeListener(timerListManagerListener)
    }

    override fun populate(title: String, isActive: Boolean) {
        this.isTimersActivated = isActive
        screen.updateTitle(title)
        updateTimerList()
    }

    private fun updateTimerList() {
        val timers = timerListManager.getTimerList().filter {
            it.isActivate == isTimersActivated
        }
        screen.updateTimerList(timers)
        screen.displayTitle(timers.isNotEmpty())
    }

    private fun createTimeManagerListener() = object : TimeManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            updateTimerList()
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            // nothing to do
        }
    }

    private fun createTimerListManagerListener() = object : TimerListManager.Listener {
        override fun onTimerRemoved(removedTimer: Timer) {
            updateTimerList()
        }

        override fun onTimerAdded(updatedTimer: Timer) {
            updateTimerList()
        }

        override fun onTimerRenamed(updatedTimer: Timer) {
            // nothing to do
        }
    }
}