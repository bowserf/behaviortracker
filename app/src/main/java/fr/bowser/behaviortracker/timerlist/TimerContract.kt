package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerState

interface TimerContract {

    interface View{

        fun displayCreateTimerView()

        fun displayTimerList(timers: ArrayList<TimerState>)

        fun onTimerRemoved(timer: Timer)

        fun onTimerAdded(timer: Timer, startNow : Boolean)

    }

    interface Presenter{

        fun start()

        fun stop()

        fun onClickAddTimer()

    }

}