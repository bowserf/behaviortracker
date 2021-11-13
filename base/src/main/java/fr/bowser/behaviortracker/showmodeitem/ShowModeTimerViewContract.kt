package fr.bowser.behaviortracker.showmodeitem

import fr.bowser.behaviortracker.timer.Timer

interface ShowModeTimerViewContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun setTimer(timer: Timer)

        fun onClickView()
    }

    interface Screen {

        fun timerUpdated(newTime: Long)

        fun statusUpdated(activate: Boolean)
    }
}
