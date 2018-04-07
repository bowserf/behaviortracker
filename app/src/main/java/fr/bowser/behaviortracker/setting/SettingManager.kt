package fr.bowser.behaviortracker.setting

interface SettingManager {

    fun getTimerModification(): Int

    fun isOneActiveTimerAtATime(): Boolean

}