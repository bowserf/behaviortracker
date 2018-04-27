package fr.bowser.behaviortracker.timer


interface TimerListManager {

    fun addTimer(timer: Timer)

    fun removeTimer(timer: Timer)

    fun getTimerList(): List<Timer>

    fun renameTimer(timer: Timer, newName: String)

    fun reorderTimerList(timerList: List<Timer>)

    fun registerTimerCallback(timerCallback: TimerCallback): Boolean

    fun unregisterTimerCallback(timerCallback: TimerCallback): Boolean

    interface TimerCallback {
        fun onTimerRemoved(updatedTimer: Timer)
        fun onTimerAdded(updatedTimer: Timer)
        fun onTimerRenamed(updatedTimer: Timer)
    }

}