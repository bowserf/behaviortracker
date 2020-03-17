package fr.bowser.behaviortracker.alarm

import fr.bowser.feature.alarm.AlarmTime

interface AlarmTimerContract {

    interface Presenter {

        fun start()

        fun onClickValidate(alarmTime: AlarmTime)
    }

    interface View {

        fun restoreAlarmStatus(alarmTime: AlarmTime)

        fun displayMessageAlarmEnabled()

        fun displayMessageAlarmDisabled()
    }
}