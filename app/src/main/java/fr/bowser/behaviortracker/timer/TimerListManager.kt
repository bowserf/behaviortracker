package fr.bowser.behaviortracker.timer


interface TimerListManager {

    fun addTimer(timerState: TimerState)

    fun removeTimer(timerState: TimerState)

    fun getTimerList(): List<TimerState>

    fun renameTimer(timerState: TimerState, newName: String)

    fun registerTimerCallback(timerCallback: TimerCallback): Boolean

    fun unregisterTimerCallback(timerCallback: TimerCallback): Boolean

    interface TimerCallback {
        fun onTimerRemoved(updatedTimerState: TimerState)
        fun onTimerAdded(updatedTimerState: TimerState)
        fun onTimerRenamed(updatedTimerState: TimerState)
    }

}