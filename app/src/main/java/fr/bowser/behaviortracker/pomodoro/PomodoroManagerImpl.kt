package fr.bowser.behaviortracker.pomodoro

import android.os.Vibrator
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class PomodoroManagerImpl(private val timeManager: TimeManager,
                          private val timerListManager: TimerListManager,
                          private val settingManager: SettingManager,
                          val pauseTimer: Timer,
                          private val vibrator: Vibrator,
                          private val isDebug: Boolean) : PomodoroManager {

    override var listener: PomodoroManager.Listener? = null

    override var pomodoroTime = 0L

    override var currentTimer: Timer? = null

    override var isRunning = false

    override var isStarted = false

    override var currentSessionDuration = 0L

    private var actionTimer: Timer? = null

    private var actionDuration = 0L

    private var pauseDuration = 0L

    private val timeManagerCallback = createTimeManagerCallback()

    private val timerCallback = createTimerCallback()

    override fun startPomodoro(actionTimer: Timer) {
        this.actionTimer = actionTimer
        this.pauseDuration = if (isDebug) 5L else settingManager.getPomodoroPauseStepDuration()
        this.actionDuration = if (isDebug) 10L else settingManager.getPomodoroStepDuration()

        currentTimer = actionTimer
        pomodoroTime = actionDuration
        currentSessionDuration = actionDuration

        isStarted = true
        isRunning = true

        timeManager.registerUpdateTimerCallback(timeManagerCallback)
        timerListManager.registerTimerCallback(timerCallback)
        timeManager.startTimer(currentTimer!!)

        listener?.onCountFinished(currentTimer!!, pomodoroTime)
        listener?.onPomodoroSessionStarted()
    }

    override fun resume() {
        if (!isStarted) {
            return
        }
        isRunning = true
        timeManager.startTimer(currentTimer!!)
        timerListManager.registerTimerCallback(timerCallback)
    }

    override fun pause() {
        if (!isStarted) {
            return
        }
        isRunning = false
        timeManager.stopTimer(currentTimer!!)
        timerListManager.unregisterTimerCallback(timerCallback)
    }

    override fun stop() {
        if (actionTimer != null) {
            timeManager.stopTimer(actionTimer!!)
        }
        isStarted = false
        actionTimer = null
        currentTimer = null
        timeManager.unregisterUpdateTimerCallback(timeManagerCallback)
    }

    private fun createTimeManagerCallback(): TimeManager.TimerCallback {
        return object : TimeManager.TimerCallback {

            override fun onTimerStateChanged(updatedTimer: Timer) {
                if (actionTimer == updatedTimer || updatedTimer == pauseTimer) {
                    listener?.onTimerStateChanged(updatedTimer)
                }
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                if (updatedTimer != currentTimer) {
                    return
                }

                pomodoroTime--

                listener?.updateTime(currentTimer!!, pomodoroTime)

                if (pomodoroTime > 0L) {
                    return
                }

                val previousTimer: Timer
                if (currentTimer == actionTimer) {
                    currentTimer = pauseTimer
                    previousTimer = actionTimer!!
                    pomodoroTime = pauseDuration
                    currentSessionDuration = pauseDuration
                } else {
                    currentTimer = actionTimer
                    previousTimer = pauseTimer
                    pomodoroTime = actionDuration
                    currentSessionDuration = actionDuration
                }

                if (settingManager.isPomodoroVibrationEnable()) {
                    vibrator.vibrate(DEFAULT_VIBRATION_DURATION)
                }

                timeManager.startTimer(currentTimer!!)
                timeManager.stopTimer(previousTimer)

                listener?.onCountFinished(currentTimer!!, pomodoroTime)
            }

        }
    }

    private fun createTimerCallback(): TimerListManager.TimerCallback {
        return object : TimerListManager.TimerCallback {
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