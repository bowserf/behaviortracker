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

        fun onClickUpdateTimer()

        fun onConfirmResetTimer(timer: Timer)
    }

    interface Screen {

        fun setTime(time: Long)

        fun setName(name: String)

        fun displayRenameDialog(oldName: String)

        fun statusUpdated(activate: Boolean)

        fun startShowMode(id: Long)

        fun displayUpdateTimerTimeDialog(timerId: Long)

        fun updateLastUpdatedDate(date: String)

        fun playSelectedAnimation()

        fun setColorId(colorId: Int)

        fun displayConfirmResetTimer(timer: Timer)
    }
}
