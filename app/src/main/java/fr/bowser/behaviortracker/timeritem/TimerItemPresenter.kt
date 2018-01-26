package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.TimerManager

class TimerItemPresenter(private val view: TimerItemContract.View, private val timerManager: TimerManager) : TimerItemContract.Presenter {

    private var isActivate = false

    private var currentTime: Long = 0

    override fun onTimerStateChange() {
        isActivate = !isActivate
        if(isActivate){
            timerManager.registerUpdateTimerCallback(updateTimerCallback)
        }else{
            timerManager.unregisterUpdateTimerCallback(updateTimerCallback)
        }
    }

    override fun onClickDecreaseTime() {
        currentTime -= DEFAULT_TIMER_MODIFICATOR
        if(currentTime < 0){
            currentTime = 0
        }
        view.timerUpdated(currentTime)
    }

    override fun onClickIncreaseTime() {
        currentTime += DEFAULT_TIMER_MODIFICATOR
        view.timerUpdated(currentTime)
    }

    private val updateTimerCallback = object:TimerManager.UpdateTimerCallback{
        override fun timeUpdated() {
            currentTime++

            view.timerUpdated(currentTime)

            //TODO notify database
        }
    }

    companion object {
        private val DEFAULT_TIMER_MODIFICATOR = 15
    }


}