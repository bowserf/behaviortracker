package fr.bowser.behaviortracker.timer

interface TimeManager {

    fun startTimer(timer: Timer)

    fun stopTimer(timer: Timer)

    fun updateTime(timer: Timer, newTime: Float)

    fun registerUpdateTimerCallback(callback: TimerCallback): Boolean

    fun unregisterUpdateTimerCallback(callback: TimerCallback)

    interface TimerCallback {
        fun onTimerStateChanged(updatedTimer: Timer)
        fun onTimerTimeChanged(updatedTimer: Timer)
    }

}