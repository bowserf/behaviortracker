package fr.bowser.behaviortracker.timeritem

import fr.bowser.behaviortracker.timer.Timer


interface TimerItemContract {

    interface View {

        fun timerUpdated(newTime: Long)

        fun nameUpdated(newName: String)

        fun displayRenameDialog(oldName: String)

        fun statusUpdated(activate: Boolean)

        fun timerRenamed(name: String)

        fun updateTimeModification(timeModification: Int)

        fun startShowMode(id: Long)

    }

    interface Presenter {

        fun start()

        fun stop()

        fun setTimer(timer: Timer)

        fun timerStateChange()

        fun onClickDecreaseTime()

        fun onClickIncreaseTime()

        fun onClickDeleteTimer()

        fun onClickResetTimer()

        fun onClickRenameTimer()

        fun onTimerNameUpdated(newTimerName: String)

        fun onClickCard()

        fun onClickStartPomodoro()

    }

}