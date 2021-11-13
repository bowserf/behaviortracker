package fr.bowser.behaviortracker.alarm

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.feature.alarm.AlarmTime
import fr.bowser.feature.alarm.AlarmTimerManager

class AlarmDialogPresenter(
    private val screen: AlarmTimerContract.Screen,
    private val alarmTimerManager: AlarmTimerManager,
    private val eventManager: EventManager
) : AlarmTimerContract.Presenter {

    override fun onStart() {
        val alarmTimer = alarmTimerManager.getSavedAlarmTimer()
        alarmTimer?.let { screen.restoreAlarmStatus(it) }
    }

    override fun onClickValidate(alarmTime: AlarmTime) {
        if (alarmTime.isActivated) {
            screen.displayMessageAlarmEnabled()
            alarmTimerManager.setAlarm(alarmTime.hour, alarmTime.minute)
        } else {
            screen.displayMessageAlarmDisabled()
            alarmTimerManager.removeAlarm()
        }
        eventManager.sendSetAlarmEvent(alarmTime.isActivated)
    }
}
