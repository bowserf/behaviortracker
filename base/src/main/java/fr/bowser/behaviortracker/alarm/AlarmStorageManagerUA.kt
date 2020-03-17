package fr.bowser.behaviortracker.alarm

import fr.bowser.feature.alarm.AlarmStorageManager
import fr.bowser.feature.alarm.AlarmTime

class AlarmStorageManagerUA : AlarmStorageManager {

    private var hour = 7
    private var minute = 30
    private var isActivated = false

    override fun saveAlarmTime(hour: Int, minute: Int, isActivated: Boolean) {
        this.hour = hour
        this.minute = minute
        this.isActivated = isActivated
    }

    override fun disableAlarmTime() {
        isActivated = false
    }

    override fun removeAlarmTime() {
        isActivated = false
        hour = 0
        minute = 0
    }

    override fun getSavedAlarmTime(): AlarmTime? {
        return AlarmTime(hour, minute, isActivated)
    }
}