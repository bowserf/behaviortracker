package fr.bowser.behaviortracker.setting

interface SettingManager {

    fun isPomodoroVibrationEnable(): Boolean

    fun getPomodoroPauseStepDuration(): Long

    fun getPomodoroStepDuration(): Long

    fun showEndedTasks(): Boolean

    fun setShowEndedTasks(show: Boolean)
    
    fun addListener(listener: Listener)
    
    fun removeListener(listener: Listener)
    
    interface Listener {
        fun onShowEndedTasksChanged()
    }
}
