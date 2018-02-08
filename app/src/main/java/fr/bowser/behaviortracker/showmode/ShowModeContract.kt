package fr.bowser.behaviortracker.showmode

import fr.bowser.behaviortracker.timer.Timer

interface ShowModeContract {

    interface View {

        fun displayTimerList(timers: List<Timer>, selectedTimerPosition: Int)

    }

    interface Presenter {

        fun start(selectedTimerId: Long)

    }

}