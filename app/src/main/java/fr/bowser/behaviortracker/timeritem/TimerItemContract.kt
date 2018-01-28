package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerState

interface TimerItemContract {

    interface View{

        fun timerUpdated(newTime: Long)

    }

    interface Presenter{

        fun onTimerStateChange()

        fun onClickDecreaseTime()

        fun onClickIncreaseTime()

        fun setTimer(timerState: TimerState)

        fun onClickDeleteTimer()

    }

}