package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.Timer

interface TimerItemContract {

    interface View{

        fun timerUpdated(newTime: Long)

    }

    interface Presenter{

        fun onTimerStateChange()

        fun onClickDecreaseTime()

        fun onClickIncreaseTime()

        fun setTimer(timer: Timer)

        fun onClickDeleteTimer()

    }

}