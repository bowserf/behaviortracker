package fr.bowser.behaviortracker.timer

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext


class TimerListManagerImpl(private val timerDAO: TimerDAO,
                           private val timeManager: TimeManager) : TimerListManager {

    internal val background = newFixedThreadPoolContext(2, "bg")

    val timersState = ArrayList<TimerState>()

    private val callbacks = ArrayList<TimerListManager.TimerCallback>()

    init {
        launch(background) {
            val timers = timerDAO.getTimers()
            timers.mapTo(timersState) { TimerState(false, it) }
        }
    }

    override fun addTimer(timerState: TimerState) {
        timersState.add(timerState)
        for (callback in callbacks) {
            callback.onTimerAdded(timerState)
        }

        launch(background) {
            timerState.timer.id = timerDAO.addTimer(timerState.timer)
        }
    }

    override fun removeTimer(timerState: TimerState) {
        timeManager.stopTimer(timerState)

        timersState.remove(timerState)
        for (callback in callbacks) {
            callback.onTimerRemoved(timerState)
        }

        launch(background) {
            timerDAO.removeTimer(timerState.timer)
        }
    }

    override fun getTimerList(): List<TimerState> {
        return timersState
    }

    override fun renameTimer(timerState: TimerState, newName: String) {
        timerState.timer.name = newName
        launch(background) {
            timerDAO.renameTimer(timerState.timer.id, newName)
        }

        for (callback in callbacks) {
            callback.onTimerRenamed(timerState)
        }
    }

    override fun registerTimerCallback(timerCallback: TimerListManager.TimerCallback): Boolean {
        if (callbacks.contains(timerCallback)) {
            return false
        }
        return callbacks.add(timerCallback)
    }

    override fun unregisterTimerCallback(timerCallback: TimerListManager.TimerCallback): Boolean {
        return callbacks.remove(timerCallback)
    }

}