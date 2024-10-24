package fr.bowser.behaviortracker.pomodoro

import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository

class PomodoroManagerImpl(
    private val timerManager: TimerManager,
    timerRepository: TimerRepository,
    private val settingManager: SettingManager,
    private val pauseTimer: Timer,
    private val vibrator: Vibrator,
    private val isDebug: Boolean,
) : PomodoroManager {

    override var listeners: MutableList<PomodoroManager.Listener> = mutableListOf()

    override var pomodoroTime = 0L

    override var currentTimer: Timer? = null

    override var isRunning = false

    override var isStarted = false

    override var currentSessionDuration = 0L

    override var isPendingState = false

    private var actionTimer: Timer? = null

    private var actionDuration = 0L

    private var pauseDuration = 0L

    private val timerManagerListener = createTimeManagerListener()

    private val timerRepositoryListener = createTimerRepositoryListener()

    private var audioAttributes: AudioAttributes? = null

    private var vibrationEffect: VibrationEffect? = null

    init {
        timerRepository.addListener(timerRepositoryListener)
        timerManager.addListener(timerManagerListener)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            vibrationEffect = VibrationEffect.createOneShot(
                DEFAULT_VIBRATION_DURATION,
                DEFAULT_AMPLITUDE,
            )
        }
    }

    override fun isBreakStep(): Boolean {
        return currentTimer == pauseTimer
    }

    override fun getBreakTimer(): Timer {
        return pauseTimer
    }

    override fun startPomodoro(actionTimer: Timer) {
        this.actionTimer = actionTimer
        this.pauseDuration = if (isDebug && !BuildConfig.UA) {
            POMODORO_DEBUG_PAUSE_STEP_DURATION
        } else {
            settingManager.getPomodoroPauseStepDuration()
        }
        this.actionDuration = if (isDebug && !BuildConfig.UA) {
            POMODORO_DEBUG_STEP_DURATION
        } else {
            settingManager.getPomodoroStepDuration()
        }

        currentTimer = actionTimer
        pomodoroTime = actionDuration
        currentSessionDuration = actionDuration

        isStarted = true
        isRunning = true

        timerManager.startTimer(currentTimer!!)

        listeners.forEach { it.onPomodoroSessionStarted(currentTimer!!, pomodoroTime) }
    }

    override fun resume() {
        if (!isStarted) {
            return
        }
        isPendingState = false
        isRunning = true
        timerManager.startTimer(currentTimer!!)
    }

    override fun pause() {
        if (!isStarted) {
            return
        }
        isRunning = false
        timerManager.stopTimer()
    }

    override fun stop() {
        if (!isStarted) {
            return
        }
        timerManager.stopTimer()
        isStarted = false
        isPendingState = false
        isRunning = false
        actionTimer = null
        currentTimer = null
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

    private fun createTimeManagerListener(): TimerManager.Listener {
        return object : TimerManager.Listener {

            override fun onTimerStateChanged(updatedTimer: Timer) {
                if (actionTimer == updatedTimer || updatedTimer == pauseTimer) {
                    isRunning = timerManager.isRunning(updatedTimer)
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

                timerManager.stopTimer()

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
                    vibrate()
                }

                isPendingState = true

                listeners.forEach { it.onCountFinished(currentTimer!!, pomodoroTime) }
            }

            override fun onTimerFinishStateChanged(timer: Timer) {
                if (!timer.isFinished) return
                if (timer != actionTimer) return
                stop()
            }
        }
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            vibrator.vibrate(DEFAULT_VIBRATION_DURATION)
        } else {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    DEFAULT_VIBRATION_DURATION,
                    DEFAULT_AMPLITUDE,
                ),
            )
        }
    }

    private fun createTimerRepositoryListener(): TimerRepository.Listener {
        return object : TimerRepository.Listener {
            override fun onTimerRemoved(removedTimer: Timer) {
                if (actionTimer != removedTimer) {
                    return
                }
                stop()
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
        private const val POMODORO_DEBUG_STEP_DURATION = 10L
        private const val POMODORO_DEBUG_PAUSE_STEP_DURATION = 5L
    }
}
