package fr.bowser.behaviortracker.setting

interface SettingManager {

    fun isPomodoroVibrationEnable(): Boolean

    fun getPomodoroPauseStepDuration(): Long

    fun getPomodoroStepDuration(): Long
}
