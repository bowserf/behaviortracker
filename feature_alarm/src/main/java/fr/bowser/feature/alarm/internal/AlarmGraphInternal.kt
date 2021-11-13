package fr.bowser.feature.alarm.internal

class AlarmGraphInternal {

    private val alarmListenerManager by lazy { AlarmListenerModule.createAlarmListener() }

    companion object {

        private var graph: AlarmGraphInternal? = null

        fun init() {
            if (graph == null) {
                graph = AlarmGraphInternal()
            }
        }

        fun getAlarmListenerManager() = graph!!.alarmListenerManager
    }
}
