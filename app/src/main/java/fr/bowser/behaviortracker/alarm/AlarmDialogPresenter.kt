package fr.bowser.behaviortracker.alarm

import fr.bowser.behaviortracker.event.EventManager

class AlarmDialogPresenter(
    private val view: AlarmTimerContract.View,
    private val alarmTimerManager: AlarmTimerManager,
    private val eventManager: EventManager
) : AlarmTimerContract.Presenter {

    override fun start() {
        val alarmTimer = alarmTimerManager.getSavedAlarmTimer()
        alarmTimer?.let { view.restoreAlarmStatus(it) }
    }

    override fun onClickValidate(alarmTime: AlarmTime) {
        if (alarmTime.isActivated) {
            view.displayMessageAlarmEnabled()
            alarmTimerManager.setAlarm(alarmTime.hour, alarmTime.minute)
        } else {
            view.displayMessageAlarmDisabled()
            alarmTimerManager.removeAlarm()
        }
        eventManager.sendSetAlarmEvent(alarmTime.isActivated)
    }
}