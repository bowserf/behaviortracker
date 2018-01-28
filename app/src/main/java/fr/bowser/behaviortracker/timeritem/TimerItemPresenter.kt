package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
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
    }

    override fun setTimer(timerState: TimerState) {
        this.timerState = timerState
        manageUpdateTimerCallback()
    }

    override fun onTimerStateChange() {
        timerState.isActivate = !timerState.isActivate
        manageUpdateTimerCallback()
    }

    override fun onClickDeleteTimer() {
        timerListManager.removeTimer(timerState.timer)
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
        timerState.timer.name = newTimerName

        //TODO update with TimerListManager
        //TODO update database
    }

    override fun onTimerAdded(timer: Timer) {

    }

    override fun onTimerRemoved(timer: Timer) {
        if (timerState.timer == timer) {
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

    private val updateTimerCallback = object : TimeManager.UpdateTimerCallback {
        override fun timeUpdated() {
            timerState.timer.currentTime++

            view.timerUpdated(timerState.timer.currentTime)

            //TODO notify database
        }
    }

    companion object {
        private val DEFAULT_TIMER_MODIFICATOR = 15
    }


}