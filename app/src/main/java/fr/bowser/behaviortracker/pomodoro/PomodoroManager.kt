package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroManager {

    var listener: Listener?

    var pomodoroTime: Long
    var currentTimer: Timer?

    fun startPomodoro(actionTimer: Timer,
                      actionDuration: Long,
                      restTimer: Timer,
                      restDuration: Long)

    fun resume()

    fun pause()

    fun stop()

    interface Listener {
        fun onTimerStateChanged(updatedTimer: Timer)
        fun updateTime(timer: Timer, currentTime: Long)
        fun onCountFinished(newTimer: Timer, duration: Long)
    }

}