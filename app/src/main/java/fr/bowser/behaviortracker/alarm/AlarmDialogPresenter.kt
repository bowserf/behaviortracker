package fr.bowser.behaviortracker.alarm

class AlarmDialogPresenter(private val view: AlarmTimerContract.View,
                           private val alarmTimerManager: AlarmTimerManager) : AlarmTimerContract.Presenter {

    override fun start() {
        val alarmTimer = alarmTimerManager.getSavedAlarmTimer()
        alarmTimer?.let { view.restoreAlarmStatus(it) }
    }

    override fun onClickValidate(alarmTime: AlarmStorageManager.AlarmTime) {
        if(alarmTime.isActivated) {
            view.displayMessageAlarmEnabled()
            alarmTimerManager.setAlarm(alarmTime.hour, alarmTime.minute)
        }else{
            view.displayMessageAlarmDisabled()
            alarmTimerManager.removeAlarm()
        }
    }

}