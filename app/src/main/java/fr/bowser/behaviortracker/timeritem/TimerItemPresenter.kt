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
        manageTimerUpdate()

        view.statusUpdated(timerState.isActivate)
    }

    override fun onClickDeleteTimer() {
        timerListManager.removeTimer(timerState)
    }

    override fun onClickDecreaseTime() {
        timeManager.updateTime(timerState, timerState.timer.currentTime - DEFAULT_TIMER_MODIFICATION)

        view.timerUpdated(timerState.timer.currentTime)
    }

    override fun onClickIncreaseTime() {
        timeManager.updateTime(timerState, timerState.timer.currentTime + DEFAULT_TIMER_MODIFICATION)

        view.timerUpdated(timerState.timer.currentTime)
    }

    override fun onClickResetTimer() {
        timeManager.updateTime(timerState, 0)

        view.timerUpdated(timerState.timer.currentTime)
    }

    override fun onClickRenameTimer() {
        view.displayRenameDialog(timerState.timer.name)
    }

    override fun onTimerNameUpdated(newTimerName: String) {
        timerListManager.renameTimer(timerState, newTimerName)

        timerNotificationManager.renameTimerNotif(timerState)
    }

    override fun onTimerAdded(updatedTimerState: TimerState) {
        // nothing to do
    }

    override fun onTimerRemoved(updatedTimerState: TimerState) {
        if (timerState == updatedTimerState) {
            timeManager.unregisterUpdateTimerCallback(updateTimerCallback)

            timerNotificationManager.destroyNotif(timerState)
        }
    }

    override fun onTimerRenamed(updatedTimerState: TimerState) {
        if (timerState == updatedTimerState) {
            view.timerRenamed(updatedTimerState.timer.name)
        }
    }

    private fun manageTimerUpdate() {
        if (!timerState.isActivate) {
            timeManager.startTimer(timerState)
        } else {
            timeManager.stopTimer(timerState)
        }
    }

    private val updateTimerCallback =
            object : TimeManager.TimerCallback {

                override fun onTimerStateChanged(updatedTimerState: TimerState) {
                    if (timerState == updatedTimerState) {
                        view.statusUpdated(updatedTimerState.isActivate)
                    }
                }

                override fun onTimerTimeChanged(updatedTimerState: TimerState) {
                    if (timerState == updatedTimerState) {
                        view.timerUpdated(updatedTimerState.timer.currentTime)
                    }
                }
            }

    companion object {
        private const val DEFAULT_TIMER_MODIFICATION = 15
    }


}