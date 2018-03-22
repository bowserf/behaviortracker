package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState

class TimerItemPresenter(private val view: TimerItemContract.View,
                         private val timeManager: TimeManager,
                         private val timerListManager: TimerListManager,
                         private val timerNotificationManager: TimerNotificationManager)
    : TimerItemContract.Presenter,
        TimerListManager.TimerCallback {

    private lateinit var timerState: TimerState

    override fun start() {
        timerListManager.registerTimerCallback(this)
        timeManager.registerUpdateTimerCallback(updateTimerCallback)

        view.timerUpdated(timerState.timer.currentTime)
        view.statusUpdated(timerState.isActivate)
    }

    override fun stop() {
        timerListManager.unregisterTimerCallback(this)
        timeManager.unregisterUpdateTimerCallback(updateTimerCallback)
    }

    override fun setTimer(timerState: TimerState) {
        this.timerState = timerState
    }

    override fun timerStateChange() {
        timerState.isActivate = !timerState.isActivate
        manageTimerUpdate()
        manageNotification()

        view.statusUpdated( timerState.isActivate)
    }

    override fun onClickDeleteTimer() {
        timerListManager.removeTimer(timerState)
    }

    override fun onClickDecreaseTime() {
        timerListManager.updateTime(timerState, timerState.timer.currentTime - DEFAULT_TIMER_MODIFICATION, true)

        view.timerUpdated(timerState.timer.currentTime)

        timerNotificationManager.updateTimeNotif(timerState)
    }

    override fun onClickIncreaseTime() {
        timerListManager.updateTime(timerState, timerState.timer.currentTime + DEFAULT_TIMER_MODIFICATION, true)

        view.timerUpdated(timerState.timer.currentTime)

        timerNotificationManager.updateTimeNotif(timerState)
    }

    override fun onClickResetTimer() {
        timerListManager.updateTime(timerState, 0, true)

        view.timerUpdated(timerState.timer.currentTime)

        timerNotificationManager.updateTimeNotif(timerState)
    }

    override fun onClickRenameTimer() {
        view.displayRenameDialog(timerState.timer.name)
    }

    override fun onTimerNameUpdated(newTimerName: String) {
        timerListManager.renameTimer(timerState, newTimerName)

        timerNotificationManager.renameTimerNotif(timerState)
    }

    override fun onTimerAdded(updatedTimerState: TimerState, position: Int) {
        // nothing to do
    }

    override fun onTimerRemoved(updatedTimerState: TimerState, position: Int) {
        if (timerState == updatedTimerState) {
            timerState.isActivate = false
            timeManager.unregisterUpdateTimerCallback(updateTimerCallback)

            timerNotificationManager.destroyNotif(timerState)
        }
    }

    override fun onTimerStateChanged(updatedTimerState: TimerState, position: Int) {
        if (timerState == updatedTimerState) {
            view.statusUpdated(updatedTimerState.isActivate)
        }
    }

    override fun onTimerTimeChanged(updatedTimerState: TimerState, position: Int) {
        if (timerState == updatedTimerState) {
            view.timerUpdated(updatedTimerState.timer.currentTime)
        }
    }

    override fun onTimerRenamed(updatedTimerState: TimerState, position: Int) {
        if (timerState == updatedTimerState) {
            view.timerRenamed(updatedTimerState.timer.name)
        }
    }

    private fun manageTimerUpdate() {
        if (timerState.isActivate) {
            timeManager.startTimer(timerState)
        } else {
            timeManager.stopTimer(timerState)
        }
    }

    private fun manageNotification() {
        if (timerState.isActivate) {
            timerNotificationManager.displayTimerNotif(timerState)
        } else {
            timerNotificationManager.pauseTimerNotif(timerState)
        }
    }

    private val updateTimerCallback =
            object : TimeManager.UpdateTimerCallback {
                override fun timeUpdated() {
                    view.timerUpdated(timerState.timer.currentTime)
                }
            }

    companion object {
        private const val DEFAULT_TIMER_MODIFICATION = 15
    }


}