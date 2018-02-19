package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.TimerState

interface TimerItemContract {

    interface View {

        fun timerUpdated(newTime: Long)

        fun nameUpdated(newName: String)

        fun displayRenameDialog(oldName: String)

        fun statusUpdated(activate: Boolean)

    }

    interface Presenter {

        fun start()

        fun stop()

        fun setTimer(timerState: TimerState)

        fun onTimerStateChange(): Boolean

        fun onClickDecreaseTime()

        fun onClickIncreaseTime()

        fun onClickDeleteTimer()

        fun onClickResetTimer()

        fun onClickRenameTimer()

        fun onTimerNameUpdated(newTimerName: String)

    }

}