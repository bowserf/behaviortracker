package fr.bowser.behaviortracker.setting

interface SettingManager {

    fun getTimerModification(): Int

    fun isOneActiveTimerAtATime(): Boolean

    fun registerTimerModificationListener(listener: TimerModificationListener)

    fun unregisterTimerModificationListener(listener: TimerModificationListener)

    interface TimerModificationListener {

        fun onTimerModificationChanged(timerModification: Int)

    }

}