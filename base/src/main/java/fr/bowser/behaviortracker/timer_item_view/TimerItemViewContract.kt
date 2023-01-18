package fr.bowser.behaviortracker.timer_item_view

import fr.bowser.behaviortracker.timer.Timer

interface TimerItemViewContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun setTimer(timer: Timer)

        fun timerStateChange()

        fun onClickResetTimer()

        fun onClickRenameTimer()

        fun onTimerNameUpdated(newTimerName: String)

        fun onClickCard()

        fun onClickStartPomodoro()

        fun onClickUpdateTimer()
    }

    interface Screen {

        fun timerUpdated(newTime: Long)

        fun nameUpdated(newName: String)

        fun displayRenameDialog(oldName: String)

        fun statusUpdated(activate: Boolean)

        fun timerRenamed(name: String)

        fun startShowMode(id: Long)

        fun displayUpdateTimerTimeDialog(timerId: Long)

        fun updateLastUpdatedDate(date: String)
    }
}
