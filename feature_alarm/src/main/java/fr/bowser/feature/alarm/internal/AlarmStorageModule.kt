package fr.bowser.feature.alarm.internal

import fr.bowser.feature.alarm.AlarmGraph
import fr.bowser.feature.alarm.AlarmStorageManager
import fr.bowser.feature.alarm.AlarmStorageManagerModule

internal object AlarmStorageModule: AlarmStorageManagerModule {

    override fun createAlarmStorageManager(): AlarmStorageManager {
        val context = AlarmGraph.getContext()
        return AlarmStorageManagerImpl(context)
    }
}