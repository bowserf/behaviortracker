package fr.bowser.behaviortracker.timer

interface TimeManager {

    fun startTimer(timer: Timer)

    fun stopTimer(timer: Timer)

    fun updateTime(timer: Timer, newTime: Float, fakeTimer:Boolean = false)

    fun addListener(listener: Listener): Boolean

    fun removeListener(listener: Listener)

    interface Listener {
        fun onTimerStateChanged(updatedTimer: Timer)
        fun onTimerTimeChanged(updatedTimer: Timer)
    }

}