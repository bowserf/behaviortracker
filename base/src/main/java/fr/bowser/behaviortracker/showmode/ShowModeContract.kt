package fr.bowser.behaviortracker.showmode

import fr.bowser.behaviortracker.timer.Timer

interface ShowModeContract {

    interface View {

        fun displayTimerList(timers: List<Timer>, selectedTimerPosition: Int)

        fun keepScreeOn(keepScreenOn: Boolean)
    }

    interface Presenter {

        fun start(selectedTimerId: Long)

        fun onClickScreeOn()

        fun onClickScreeOff()

        fun keepScreenOn(): Boolean
    }
}