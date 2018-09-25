package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroManager {

    var listener: Listener?

    var pomodoroTime: Long

    var currentTimer: Timer?

    var isRunning: Boolean

    var isStarted: Boolean

    fun startPomodoro(actionTimer: Timer)

    fun resume()

    fun pause()

    fun stop()

    interface Listener {
        fun onPomodoroSessionStarted()
        fun onTimerStateChanged(updatedTimer: Timer)
        fun updateTime(timer: Timer, currentTime: Long)
        fun onCountFinished(newTimer: Timer, duration: Long)
    }

}