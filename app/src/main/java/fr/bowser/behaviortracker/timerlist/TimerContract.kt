package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer

interface TimerContract {

    interface View{

        fun displayCreateTimerView()

        fun displayTimerList(timers: ArrayList<Timer>)

    }

    interface Presenter{

        fun start()

        fun onClickAddTimer()

    }

}