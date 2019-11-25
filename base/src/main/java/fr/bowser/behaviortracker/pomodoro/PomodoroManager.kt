package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroManager {

    var listeners: MutableList<Listener>

    var pomodoroTime: Long

    var currentTimer: Timer?

    var isRunning: Boolean

    var isStarted: Boolean

    var currentSessionDuration: Long

    var isPendingState: Boolean

    fun startPomodoro(actionTimer: Timer)

    fun resume()

    fun pause()

    fun stop()

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {
        fun onPomodoroSessionStarted(newTimer: Timer, duration: Long)
        fun onPomodoroSessionStop()
        fun onTimerStateChanged(updatedTimer: Timer)
        fun updateTime(timer: Timer, currentTime: Long)
        fun onCountFinished(newTimer: Timer, duration: Long)
    }
}