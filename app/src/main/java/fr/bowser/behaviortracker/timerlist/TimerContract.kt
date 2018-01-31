package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.TimerState

interface TimerContract {

    interface View{

        fun displayCreateTimerView()

        fun displayTimerList(timers: ArrayList<TimerState>)

        fun onTimerRemoved(timer: TimerState, position:Int)

        fun onTimerAdded(timer: TimerState, position: Int)

    }

    interface Presenter{

        fun start()

        fun stop()

        fun onClickAddTimer()

    }

}