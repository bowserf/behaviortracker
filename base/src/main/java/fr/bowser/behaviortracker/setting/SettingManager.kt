package fr.bowser.behaviortracker.setting

interface SettingManager {

    fun getTimerModification(): Int

    fun isOneActiveTimerAtATime(): Boolean

    fun isPomodoroVibrationEnable(): Boolean

    fun getPomodoroPauseStepDuration(): Long

    fun getPomodoroStepDuration(): Long

    fun registerTimerModificationListener(listener: TimerModificationListener)

    fun unregisterTimerModificationListener(listener: TimerModificationListener)

    interface TimerModificationListener {

        fun onTimerModificationChanged(timerModification: Int)
    }
}