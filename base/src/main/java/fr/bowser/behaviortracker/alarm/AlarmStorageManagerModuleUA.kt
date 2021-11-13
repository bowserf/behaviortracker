package fr.bowser.behaviortracker.alarm

import fr.bowser.feature.alarm.AlarmStorageManager
import fr.bowser.feature.alarm.AlarmStorageManagerModule

class AlarmStorageManagerModuleUA : AlarmStorageManagerModule {

    override fun createAlarmStorageManager(): AlarmStorageManager {
        return AlarmStorageManagerUA()
    }
}
