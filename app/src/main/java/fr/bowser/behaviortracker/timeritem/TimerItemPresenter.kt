package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimeManager

class TimerItemPresenter(private val view: TimerItemContract.View, private val timeManager: TimeManager) : TimerItemContract.Presenter {

    private var isActivate = false

    private lateinit var timer: Timer

    override fun onTimerStateChange() {
        isActivate = !isActivate
        if(isActivate){
            timeManager.registerUpdateTimerCallback(updateTimerCallback)
        }else{
            timeManager.unregisterUpdateTimerCallback(updateTimerCallback)
        }
    }

    override fun setTimer(timer: Timer) {
        this.timer = timer
    }

    override fun onClickDecreaseTime() {
        timer.currentTime -= DEFAULT_TIMER_MODIFICATOR
        if(timer.currentTime < 0){
            timer.currentTime = 0
        }
        view.timerUpdated(timer.currentTime)
    }

    override fun onClickIncreaseTime() {
        timer.currentTime += DEFAULT_TIMER_MODIFICATOR
        view.timerUpdated(timer.currentTime)
    }

    private val updateTimerCallback = object: TimeManager.UpdateTimerCallback{
        override fun timeUpdated() {
            timer.currentTime++

            view.timerUpdated(timer.currentTime)

            //TODO notify database
        }
    }

    companion object {
        private val DEFAULT_TIMER_MODIFICATOR = 15
    }


}