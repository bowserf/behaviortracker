package fr.bowser.behaviortracker.showmodeitem

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer

class ShowModeTimerViewPresenter(val view: ShowModeTimerViewContract.View,
                                 val timeManager: TimeManager)
    : ShowModeTimerViewContract.Presenter {

    private lateinit var timer: Timer

    override fun start(){
        timeManager.registerUpdateTimerCallback(updateTimerCallback)
    }

    override fun stop() {
        timeManager.unregisterUpdateTimerCallback(updateTimerCallback)
    }

    override fun setTimer(timer: Timer) {
        this.timer = timer
    }

    override fun onClickView() {
        if(timer.isActivate){
            timeManager.stopTimer(timer)
        }else{
            timeManager.startTimer(timer)
        }
    }

    private val updateTimerCallback = object : TimeManager.TimerCallback {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            // nothing to do
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if(updatedTimer == timer) {
                view.timerUpdated(timer.currentTime)
            }
        }

    }

}