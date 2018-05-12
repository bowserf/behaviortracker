package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer

class PomodoroManager(private val timeManager: TimeManager) {

    var actionTimer: Timer? = null
        private set
    var restTimer: Timer? = null
        private set

    var pomodoroTime = 0L
        private set
    var currentTimer: Timer? = null
        private set

    var isRunning = false
        private set
    var isStarted = false
        private set

    var callback: Callback? = null

    private var actionDuration = 0L
    private var restDuration = 0L

    fun startPomodoro(actionTimer: Timer,
                      actionDuration: Long,
                      restTimer: Timer,
                      restDuration: Long) {
        this.actionTimer = actionTimer
        this.restTimer = restTimer
        this.actionDuration = actionDuration
        this.restDuration = restDuration

        currentTimer = actionTimer
        pomodoroTime = actionDuration

        isStarted = true
        isRunning = true

        timeManager.registerUpdateTimerCallback(timeManagerCallback)

        timeManager.startTimer(currentTimer!!)
    }

    fun resume() {
        if (!isStarted) {
            return
        }
        isRunning = true
        timeManager.startTimer(currentTimer!!)
    }

    fun pause() {
        if (!isStarted) {
            return
        }
        isRunning = false
        timeManager.stopTimer(currentTimer!!)
    }

    fun stop() {
        isStarted = false
        actionTimer = null
        restTimer = null
        timeManager.unregisterUpdateTimerCallback(timeManagerCallback)
    }

    private val timeManagerCallback = object : TimeManager.TimerCallback {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            if (actionTimer == updatedTimer || updatedTimer == restTimer) {
                callback?.onTimerStateChanged(updatedTimer)
            }
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if (updatedTimer != currentTimer) {
                return
            }

            pomodoroTime--
            callback?.updateTime(currentTimer!!, pomodoroTime)

            if (pomodoroTime > 0L) {
                return
            }

            if (currentTimer == actionTimer) {
                currentTimer = restTimer
            } else if (currentTimer == restTimer) {
                currentTimer = actionTimer
            }
            callback?.onCountFinished(currentTimer!!)
        }
    }

    interface Callback {
        fun onTimerStateChanged(updatedTimer: Timer)
        fun updateTime(timer: Timer, currentTime: Long)
        fun onCountFinished(newTimer: Timer)
    }

}