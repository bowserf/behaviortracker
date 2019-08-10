package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager

class HomePresenter(
    private val view: HomeContract.View,
    private val timerNotificationManager: TimerNotificationManager,
    private val timeManager: TimeManager,
    private val timerListManager: TimerListManager,
    private val homeManager: HomeManager,
    private val eventManager: EventManager
) : HomeContract.Presenter {

    override fun initialize() {
        if (homeManager.getCurrentView() == HomeManager.SELECTED_HOME_VIEW_TIMERS_LIST) {
            view.displayTimerView()
        } else {
            view.displayPomodoroView()
        }
    }

    override fun start() {
        timerNotificationManager.changeOngoingState(false)
    }

    override fun stop() {
        timerNotificationManager.changeOngoingState(true)
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

    override fun onClickRewards() {
        view.displayRewardsView()
    }

    override fun onAlarmNotificationClicked() {
        eventManager.sendAlarmNotificationClickedEvent()
    }

    override fun onClickTimerView() {
        homeManager.setCurrentView(HomeManager.SELECTED_HOME_VIEW_TIMERS_LIST)
        view.displayTimerView()
    }

    override fun onClickPomodoroView() {
        homeManager.setCurrentView(HomeManager.SELECTED_HOME_VIEW_POMODORO)
        view.displayPomodoroView()
    }
}