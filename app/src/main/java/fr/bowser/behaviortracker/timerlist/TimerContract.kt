package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer

interface TimerContract {

    interface Screen {

        fun displayCreateTimerView()

        fun displayTimerList(timers: List<Timer>)

        fun onTimerRemoved(timer: Timer)

        fun onTimerAdded(timer: Timer)

        fun displayEmptyListView()

        fun displayListView()

        fun displayCancelDeletionView()

        fun isTimerListEmpty(): Boolean

    }

    interface Presenter {

        fun init()

        fun start()

        fun stop()

        fun onClickAddTimer()

        fun onTimerSwiped(timer: Timer)

        fun definitivelyRemoveTimer()

        fun onReorderFinished(timerList: List<Timer>)

        fun cancelTimerDeletion()

    }

}