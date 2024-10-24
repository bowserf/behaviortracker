package fr.bowser.behaviortracker.timer

interface TimerManager {

    fun startTimer(timer: Timer, fakeTimer: Boolean = false)

    fun stopTimer(fakeTimer: Boolean = false)

    fun isRunning(timer: Timer): Boolean

    fun getStartedTimer(): Timer?

    fun updateTime(timer: Timer, newTime: Float, fakeTimer: Boolean = false)

    fun resetTime(timer: Timer, fakeTimer: Boolean = false)

    fun updateFinishState(timer: Timer, fakeTimer: Boolean = false)

    fun addListener(listener: Listener): Boolean

    fun removeListener(listener: Listener)

    interface Listener {

        fun onTimerStateChanged(updatedTimer: Timer)

        fun onTimerTimeChanged(updatedTimer: Timer)

        fun onTimerFinishStateChanged(timer: Timer)
    }
}
