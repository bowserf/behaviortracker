package fr.bowser.behaviortracker.timer

interface TimerListManager {

    fun addTimer(timer: Timer)

    fun removeTimer(timer: Timer)

    fun removeAllTimers()

    fun getTimerList(): List<Timer>

    fun renameTimer(timer: Timer, newName: String)

    fun reorderTimerList(timerList: List<Timer>)

    fun addListener(listener: Listener): Boolean

    fun removeListener(listener: Listener): Boolean

    interface Listener {
        fun onTimerRemoved(removedTimer: Timer)
        fun onTimerAdded(updatedTimer: Timer)
        fun onTimerRenamed(updatedTimer: Timer)
    }
}