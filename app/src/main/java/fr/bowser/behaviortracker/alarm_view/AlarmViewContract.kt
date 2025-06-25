package fr.bowser.behaviortracker.alarm_view

import fr.bowser.feature.alarm.AlarmTime

interface AlarmViewContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun onClickValidate(alarmTime: AlarmTime)
    }

    interface Screen {

        fun restoreAlarmStatus(alarmTime: AlarmTime)

        fun displayMessageAlarmEnabled()

        fun displayMessageAlarmDisabled()
    }
}
