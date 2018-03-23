package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.TimerState

interface TimerContract {

    interface View {

        fun displayCreateTimerView()

        fun displayTimerList(timers: ArrayList<TimerState>)

        fun onTimerRemoved(timer: TimerState)

        fun onTimerAdded(timer: TimerState)

        fun displayEmptyListView()

        fun displayListView()

    }

    interface Presenter {

        fun start()

        fun stop()

        fun onClickAddTimer()

    }

}