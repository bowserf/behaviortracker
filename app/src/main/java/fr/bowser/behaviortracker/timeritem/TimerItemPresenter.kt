package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState

class TimerItemPresenter(private val view: TimerItemContract.View,
                         private val timeManager: TimeManager,
                         private val timerListManager: TimerListManager)
    : TimerItemContract.Presenter, TimerListManager.TimerCallback {

    private lateinit var timerState: TimerState

    override fun start() {
        timerListManager.registerTimerCallback(this)
    }

    override fun stop() {
        timerListManager.unregisterTimerCallback(this)
        timeManager.unregisterUpdateTimerCallback(updateTimerCallback)
    }

    override fun setTimer(timerState: TimerState) {
        this.timerState = timerState
        manageUpdateTimerCallback()
    }

    override fun onTimerStateChange(): Boolean {
        timerState.isActivate = !timerState.isActivate
        manageUpdateTimerCallback()
        return timerState.isActivate
    }

    override fun onClickDeleteTimer() {
        timerListManager.removeTimer(timerState)
    }

    override fun onClickDecreaseTime() {
        timerState.timer.currentTime -= DEFAULT_TIMER_MODIFICATOR
        if (timerState.timer.currentTime < 0) {
            timerState.timer.currentTime = 0
        }
        view.timerUpdated(timerState.timer.currentTime)
    }

    override fun onClickIncreaseTime() {
        timerState.timer.currentTime += DEFAULT_TIMER_MODIFICATOR
        view.timerUpdated(timerState.timer.currentTime)
    }

    override fun onClickResetTimer() {
        timerState.timer.currentTime = 0
        view.timerUpdated(timerState.timer.currentTime)
    }

    override fun onClickRenameTimer() {
        view.displayRenameDialog(timerState.timer.name)
    }

    override fun onTimerNameUpdated(newTimerName: String) {
        timerListManager.renameTimer(timerState.timer.id, newTimerName)
    }

    override fun onTimerAdded(timer: TimerState, position: Int) {
        // nothing to do
    }

    override fun onTimerRemoved(timer: TimerState, position:Int) {
        if (timerState == timer) {
            timerState.isActivate = false
            timeManager.unregisterUpdateTimerCallback(updateTimerCallback)
        }
    }

    private fun manageUpdateTimerCallback(){
        if (timerState.isActivate) {
            timeManager.registerUpdateTimerCallback(updateTimerCallback)
        } else {
            timeManager.unregisterUpdateTimerCallback(updateTimerCallback)
        }
    }

    private val updateTimerCallback = object:TimeManager.UpdateTimerCallback{
        override fun timeUpdated() {
            view.timerUpdated(timerState.timer.currentTime)
        }
    }

    companion object {
        private val DEFAULT_TIMER_MODIFICATOR = 15
    }


}