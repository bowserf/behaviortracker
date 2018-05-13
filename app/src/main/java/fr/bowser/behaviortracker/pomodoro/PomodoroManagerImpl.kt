package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer

class PomodoroManagerImpl(private val timeManager: TimeManager) : PomodoroManager {

    private var actionTimer: Timer? = null

    private var restTimer: Timer? = null

    private var currentTimer: Timer? = null

    private var pomodoroTime = 0L

    private var isRunning = false

    private var isStarted = false

    private var callbacks = ArrayList<PomodoroManager.Callback>()

    private var actionDuration = 0L
    private var restDuration = 0L

    override fun startPomodoro(actionTimer: Timer,
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

    override fun resume() {
        if (!isStarted) {
            return
        }
        isRunning = true
        timeManager.startTimer(currentTimer!!)
    }

    override fun pause() {
        if (!isStarted) {
            return
        }
        isRunning = false
        timeManager.stopTimer(currentTimer!!)
    }

    override fun stop() {
        isStarted = false
        actionTimer = null
        restTimer = null
        timeManager.unregisterUpdateTimerCallback(timeManagerCallback)
    }

    override fun resetPomodoroTimer() {
        pomodoroTime = if (currentTimer == actionTimer) actionDuration else restDuration
    }

    override fun getPomodoroActionTimer(): Timer? {
        return actionTimer
    }

    override fun getPomodoroRestTimer(): Timer? {
        return restTimer
    }

    override fun isPomodoroStarted(): Boolean {
        return isStarted
    }

    override fun isPomodoroRunning(): Boolean {
        return isRunning
    }

    override fun getPomodoroCurrentTimer(): Timer? {
        return currentTimer
    }

    override fun getPomodoroTime(): Long {
        return pomodoroTime
    }

    override fun addPomodoroCallback(callback: PomodoroManager.Callback) {
        if (callbacks.contains(callback)) {
            return
        }
        this.callbacks.add(callback)
    }

    override fun removePomodoroCallback(callback: PomodoroManager.Callback) {
        callbacks.remove(callback)
    }

    private val timeManagerCallback = object : TimeManager.TimerCallback {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            if (actionTimer == updatedTimer || updatedTimer == restTimer) {
                callbacks.forEach { it.onTimerStateChanged(updatedTimer) }
            }
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if (updatedTimer != currentTimer) {
                return
            }

            pomodoroTime--
            callbacks.forEach { it.updateTime(currentTimer!!, pomodoroTime) }

            if (pomodoroTime > 0L) {
                return
            }

            val previousTimer: Timer
            if (currentTimer == actionTimer) {
                currentTimer = restTimer
                previousTimer = actionTimer!!
                pomodoroTime = restDuration
            } else {
                currentTimer = actionTimer
                previousTimer = restTimer!!
                pomodoroTime = actionDuration
            }

            timeManager.startTimer(currentTimer!!)
            timeManager.stopTimer(previousTimer)

            callbacks.forEach { it.onCountFinished(currentTimer!!) }
        }
    }

}