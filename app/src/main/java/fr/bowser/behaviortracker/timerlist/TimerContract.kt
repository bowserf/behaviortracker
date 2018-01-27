package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer

interface TimerContract {

    interface View{

        fun displayCreateTimerView()

        fun displayTimerList(timers: ArrayList<Timer>)

        fun onTimerRemoved(timer: Timer)

        fun onTimerAdded(timer: Timer)

    }

    interface Presenter{

        fun start()

        fun stop()

        fun onClickAddTimer()

    }

}