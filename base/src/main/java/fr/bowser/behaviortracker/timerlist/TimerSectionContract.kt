package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer

interface TimerSectionContract {

    interface Presenter {
        fun onResume()
        fun onPause()
        fun populate(title: String, isActive: Boolean)
    }

    interface Screen {
        fun updateTitle(title: String)
        fun updateTimerList(timers: List<Timer>)
        fun displayTitle(display: Boolean)
    }
}