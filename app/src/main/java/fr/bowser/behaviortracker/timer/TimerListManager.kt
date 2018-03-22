package fr.bowser.behaviortracker.timer

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext


class TimerListManager(private val timerDAO: TimerDAO) {

    internal val background = newFixedThreadPoolContext(2, "bg")

    val timersState = ArrayList<TimerState>()

    private val callbacks = ArrayList<TimerCallback>()

    init {
        launch(background) {
            val timers = timerDAO.getTimers()
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
            timerState.timer.id = timerDAO.addTimer(timerState.timer)
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

    fun updateTimerState(timerState: TimerState, isActivate: Boolean) {
        timerState.isActivate = isActivate

        val position = timersState.indexOf(timerState)
        for (callback in callbacks) {
            callback.onTimerStateChanged(timerState, position)
        }
    }

    fun updateTime(timerState: TimerState, newTime: Long, notifyListeners: Boolean) {
        var currentNewTime = newTime
        if (currentNewTime < 0) {
            currentNewTime = 0
        }

        timerState.timer.currentTime = currentNewTime

        launch(background) {
            timerDAO.updateTimerTime(timerState.timer.id, timerState.timer.currentTime)
        }

        if (notifyListeners) {
            val position = timersState.indexOf(timerState)
            for (callback in callbacks) {
                callback.onTimerTimeChanged(timerState, position)
            }
        }
    }

    fun renameTimer(timerState: TimerState, newName: String) {
        timerState.timer.name = newName
        launch(background) {
            timerDAO.renameTimer(timerState.timer.id, newName)
        }

        val position = timersState.indexOf(timerState)
        for (callback in callbacks) {
            callback.onTimerRenamed(timerState, position)
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
        fun onTimerRemoved(updatedTimerState: TimerState, position: Int)
        fun onTimerAdded(updatedTimerState: TimerState, position: Int)
        fun onTimerRenamed(updatedTimerState: TimerState, position: Int)
        fun onTimerStateChanged(updatedTimerState: TimerState, position: Int)
        fun onTimerTimeChanged(updatedTimerState: TimerState, position: Int)
    }

}