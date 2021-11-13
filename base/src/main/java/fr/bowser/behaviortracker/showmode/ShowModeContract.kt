package fr.bowser.behaviortracker.showmode

import fr.bowser.behaviortracker.timer.Timer

interface ShowModeContract {

    interface Presenter {

        fun onStart(selectedTimerId: Long)

        fun onClickScreeOn()

        fun onClickScreeOff()

        fun keepScreenOn(): Boolean
    }

    interface Screen {

        fun displayTimerList(timers: List<Timer>, selectedTimerPosition: Int)

        fun keepScreeOn(keepScreenOn: Boolean)
    }
}
