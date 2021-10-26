package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer

interface TimerSectionContract {

    interface Presenter {
        fun populate(title: String, isActive: Boolean)
        fun onResume()
        fun onPause()
        fun onTimerSwiped(timerPosition: Int)
        fun onClickCancelTimerDeletion()
    }

    interface Screen {
        fun updateTitle(title: String)
        fun updateTimerList(timers: List<Timer>)
        fun displayTitle(display: Boolean)
        fun displayCancelDeletionView(cancelDuration: Int)
    }
}