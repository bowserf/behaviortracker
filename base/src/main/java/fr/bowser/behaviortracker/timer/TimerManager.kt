package fr.bowser.behaviortracker.timer

interface TimerManager {

    fun startTimer(timer: Timer)

    fun stopTimer()

    fun isRunning(timer: Timer): Boolean

    fun getStartedTimer(): Timer?

    fun updateTime(timer: Timer, newTime: Float)

    fun resetTime(timer: Timer)

    fun updateFinishState(timer: Timer)

    fun addListener(listener: Listener): Boolean

    fun removeListener(listener: Listener)

    interface Listener {

        fun onTimerStateChanged(updatedTimer: Timer)

        fun onTimerTimeChanged(updatedTimer: Timer)

        fun onTimerFinishStateChanged(timer: Timer)
    }
}
