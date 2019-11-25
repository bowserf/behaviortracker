package fr.bowser.behaviortracker.alarm

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