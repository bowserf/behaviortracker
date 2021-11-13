package fr.bowser.feature.alarm

import android.annotation.SuppressLint
import android.content.Context
import fr.bowser.feature.alarm.internal.AlarmGraphInternal
import fr.bowser.feature.alarm.internal.AlarmStorageModule
import fr.bowser.feature.alarm.internal.AlarmTimerModule

class AlarmGraph(private val context: Context) {

    private val alarmStorageManagerLazy = lazy { createAlarmStorageManager() }
    private val alarmStorageManager by alarmStorageManagerLazy

    private val alarmTimerManager by lazy { AlarmTimerModule.createAlarmTimerManager() }

    private fun createAlarmStorageManager(): AlarmStorageManager {
        return if (alarmStorageManagerModule != null) {
            alarmStorageManagerModule!!.createAlarmStorageManager()
        } else {
            AlarmStorageModule.createAlarmStorageManager()
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var graph: AlarmGraph? = null

        private var alarmStorageManagerModule: AlarmStorageManagerModule? = null

        fun init(context: Context) {
            if (graph == null) {
                graph = AlarmGraph(context)
                AlarmGraphInternal.init()
            }
        }

        fun inject(alarmStorageManagerModule: AlarmStorageManagerModule) {
            if (graph!!.alarmStorageManagerLazy.isInitialized()) {
                throw Exception("An AlarmStorageManager instance already exists.")
            }
            this.alarmStorageManagerModule = alarmStorageManagerModule
        }

        internal fun getContext() = graph!!.context
        internal fun getAlarmStorageManager() = graph!!.alarmStorageManager
        fun getAlarmTimerManager() = graph!!.alarmTimerManager
    }
}
