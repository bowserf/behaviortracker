package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager

class HomePresenter(private val view: HomeContract.View,
                    private val timerNotificationManager: TimerNotificationManager,
                    private val timeManager: TimeManager,
                    private val timerListManager: TimerListManager,
                    private val eventManager: EventManager)
    : HomeContract.Presenter {

    override fun initialize() {
        view.displayTimerView()
    }

    override fun start() {
        timerNotificationManager.changeNotifOngoing(false)
    }

    override fun stop() {
        timerNotificationManager.changeNotifOngoing(true)
    }

    override fun onClickResetAll() {
        view.displayResetAllDialog()
    }

    override fun onClickResetAllTimers() {
        val timers = timerListManager.getTimerList()
        timers.forEach { timer ->
            timeManager.updateTime(timer, 0f)
            timeManager.stopTimer(timer)
        }
    }

    override fun onClickSettings() {
        view.displaySettingsView()
    }

    override fun onClickAlarm() {
        view.displayAlarmTimerDialog()
    }

    override fun onAlarmNotificationClicked() {
        eventManager.sendAlarmNotificationClickedEvent()
    }

    override fun onClickTimerView() {
        view.displayTimerView()
    }

    override fun onClickPomodoroView() {
        view.displayPomodoroView()
    }

}