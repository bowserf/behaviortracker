package fr.bowser.behaviortracker.timer

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext


class TimerListManager(private val timerDAO: TimerDAO) {

    internal val background = newFixedThreadPoolContext(2, "bg")

    val timersState = ArrayList<TimerState>()

    private val callbacks = ArrayList<TimerCallback>()

    init {
        launch(background) {
            val timers = timerDAO.getCategories()
            timers.mapTo(timersState) { TimerState(false, it) }
        }
    }

    fun addTimer(timerState: TimerState) {
        timersState.add(timerState)
        val position = timersState.indexOf(timerState)
        for (callback in callbacks) {
            callback.onTimerAdded(timerState, position)
        }

        launch(background) {
            timerDAO.addTimer(timerState.timer)
        }
    }

    fun removeTimer(timerState: TimerState) {
        val position = timersState.indexOf(timerState)
        timersState.remove(timerState)
        for (callback in callbacks) {
            callback.onTimerRemoved(timerState, position)
        }

        launch(background) {
            timerDAO.removeTimer(timerState.timer)
        }
    }

    fun registerTimerCallback(timerCallback: TimerCallback): Boolean {
        if (callbacks.contains(timerCallback)) {
            return false
        }
        return callbacks.add(timerCallback)
    }

    fun unregisterTimerCallback(timerCallback: TimerCallback): Boolean {
        return callbacks.remove(timerCallback)
    }

    interface TimerCallback {
        fun onTimerRemoved(timer: TimerState, position: Int)
        fun onTimerAdded(timer: TimerState, position: Int)
    }

}