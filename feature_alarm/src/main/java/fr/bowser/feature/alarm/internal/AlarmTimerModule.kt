package fr.bowser.feature.alarm.internal

import fr.bowser.feature.alarm.AlarmGraph
import fr.bowser.feature.alarm.AlarmTimerManager

internal object AlarmTimerModule {

    fun createAlarmTimerManager(): AlarmTimerManager {
        val context = AlarmGraph.getContext()
        val alarmStorageManager = AlarmGraph.getAlarmStorageManager()
        val alarmListener = AlarmGraphInternal.getAlarmListenerManager()
        return AlarmTimerManagerImpl(context, alarmStorageManager, alarmListener)
    }
}