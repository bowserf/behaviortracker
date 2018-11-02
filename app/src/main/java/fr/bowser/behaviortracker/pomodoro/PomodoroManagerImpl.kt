package fr.bowser.behaviortracker.pomodoro

import android.os.Vibrator
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class PomodoroManagerImpl(private val timeManager: TimeManager,
                          timerListManager: TimerListManager,
                          private val settingManager: SettingManager,
                          val pauseTimer: Timer,
                          private val vibrator: Vibrator,
                          private val isDebug: Boolean) : PomodoroManager {

    override var listeners: MutableList<PomodoroManager.Listener> = mutableListOf()

    override var pomodoroTime = 0L

    override var currentTimer: Timer? = null

    override var isRunning = false

    override var isStarted = false

    override var currentSessionDuration = 0L

    private var actionTimer: Timer? = null

    private var actionDuration = 0L

    private var pauseDuration = 0L

    private val timeManagerListener = createTimeManagerListener()

    private val timerListManagerListener = createTimerListManagerListener()

    init {
        timerListManager.addListener(timerListManagerListener)
    }

    override fun startPomodoro(actionTimer: Timer) {
        this.actionTimer = actionTimer
        this.pauseDuration = if (isDebug) 5L else settingManager.getPomodoroPauseStepDuration()
        this.actionDuration = if (isDebug) 10L else settingManager.getPomodoroStepDuration()

        currentTimer = actionTimer
        pomodoroTime = actionDuration
        currentSessionDuration = actionDuration

        isStarted = true
        isRunning = true

        timeManager.addListener(timeManagerListener)
        timeManager.startTimer(currentTimer!!)

        listeners.forEach { it.onPomodoroSessionStarted(currentTimer!!, pomodoroTime) }
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
        if (!isStarted) {
            return
        }
        timeManager.stopTimer(currentTimer!!)
        isStarted = false
        isRunning = false
        actionTimer = null
        currentTimer = null
        timeManager.removeListener(timeManagerListener)
        listeners.forEach { it.onPomodoroSessionStop() }
    }

    override fun addListener(listener: PomodoroManager.Listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    override fun removeListener(listener: PomodoroManager.Listener) {
        listeners.remove(listener)
    }

    private fun createTimeManagerListener(): TimeManager.Listener {
        return object : TimeManager.Listener {

            override fun onTimerStateChanged(updatedTimer: Timer) {
                if (actionTimer == updatedTimer || updatedTimer == pauseTimer) {
                    listeners.forEach { it.onTimerStateChanged(updatedTimer) }
                }
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                if (updatedTimer != currentTimer) {
                    return
                }

                pomodoroTime--

                listeners.forEach { it.updateTime(currentTimer!!, pomodoroTime) }

                if (pomodoroTime > 0L) {
                    return
                }

                timeManager.stopTimer(currentTimer!!)

                if (currentTimer == actionTimer) {
                    currentTimer = pauseTimer
                    pomodoroTime = pauseDuration
                    currentSessionDuration = pauseDuration
                } else {
                    currentTimer = actionTimer
                    pomodoroTime = actionDuration
                    currentSessionDuration = actionDuration
                }

                if (settingManager.isPomodoroVibrationEnable()) {
                    vibrator.vibrate(DEFAULT_VIBRATION_DURATION)
                }

                listeners.forEach { it.onCountFinished(currentTimer!!, pomodoroTime) }
            }

        }
    }

    private fun createTimerListManagerListener(): TimerListManager.Listener {
        return object : TimerListManager.Listener {
            override fun onTimerRemoved(removedTimer: Timer) {
                if (actionTimer == removedTimer) {
                    stop()
                }
            }

            override fun onTimerAdded(updatedTimer: Timer) {
                // nothing to do
            }

            override fun onTimerRenamed(updatedTimer: Timer) {
                // nothing to do
            }

        }
    }

    companion object {
        private const val DEFAULT_VIBRATION_DURATION = 500L
    }

}