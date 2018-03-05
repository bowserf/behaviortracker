package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.timer.TimerListManager

class HomePresenter(private val view: HomeContract.View,
                    private val timerNotificationManager: TimerNotificationManager,
                    private val timerListManager: TimerListManager)
    : HomeContract.Presenter {

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
        val timerState = timerListManager.timersState
        timerState.forEach { timer ->
            timerListManager.updateTime(timer, 0, true)
            timerListManager.updateTimerState(timer, false)
            timerNotificationManager.updateTimerNotif(timer)
        }
    }
}