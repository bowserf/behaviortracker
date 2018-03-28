package fr.bowser.behaviortracker.timer

interface TimeManager {

    fun startTimer(timerState: TimerState)

    fun stopTimer(timerState: TimerState)

    fun updateTime(timerState: TimerState, newTime: Long)

    fun registerUpdateTimerCallback(callback: TimerCallback): Boolean

    fun unregisterUpdateTimerCallback(callback: TimerCallback)

    interface TimerCallback {
        fun onTimerStateChanged(updatedTimerState: TimerState)
        fun onTimerTimeChanged(updatedTimerState: TimerState)
    }

}