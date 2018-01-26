package fr.bowser.behaviortracker.timeritem

interface TimerItemContract {

    interface View{

        fun timerUpdated(newTime: Long)

    }

    interface Presenter{

        fun onTimerStateChange()

        fun onClickDecreaseTime()

        fun onClickIncreaseTime()

    }

}