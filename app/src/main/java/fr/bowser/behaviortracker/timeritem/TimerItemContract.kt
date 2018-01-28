package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.TimerState

interface TimerItemContract {

    interface View {

        fun timerUpdated(newTime: Long)

        fun nameUpdated(newName: String)

        fun displayRenameDialog(oldName: String)

    }

    interface Presenter {

        fun start()

        fun stop()

        fun onTimerStateChange()

        fun onClickDecreaseTime()

        fun onClickIncreaseTime()

        fun setTimer(timerState: TimerState)

        fun onClickDeleteTimer()

        fun onClickResetTimer()

        fun onClickRenameTimer()

        fun onTimerNameUpdated(newTimerName: String)

    }

}