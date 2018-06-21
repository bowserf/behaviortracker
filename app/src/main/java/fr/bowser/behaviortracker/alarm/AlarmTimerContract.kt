package fr.bowser.behaviortracker.alarm

interface AlarmTimerContract {

    interface Presenter {

        fun start()

        fun onClickValidate(alarmTime: AlarmStorageManager.AlarmTime)

    }

    interface View {

        fun restoreAlarmStatus(alarmTime: AlarmStorageManager.AlarmTime)

        fun displayMessageAlarmEnabled()

        fun displayMessageAlarmDisabled()

    }

}