package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer

interface TimerContract {

    interface Screen {

        fun displayCreateTimerView()

        fun displayTimerList(timers: List<Timer>)

        fun onTimerRemoved(timer: Timer)

        fun onTimerAdded(timer: Timer)

        fun displayEmptyListView()

        fun displayListView()

        fun displayCancelDeletionView()

        fun isTimerListEmpty(): Boolean

        fun displayResetAllDialog()

        fun displaySettingsView()

        fun displayAlarmTimerDialog()

        fun displayRewardsView()

        fun displayRemoveAllTimersConfirmationDialog()
    }

    interface Presenter {

        fun init()

        fun start()

        fun stop()

        fun onClickResetAll()

        fun onClickResetAllTimers()

        fun onClickSettings()

        fun onClickRemoveAllTimers()

        fun onClickConfirmRemoveAllTimers()

        fun onClickAlarm()

        fun onClickRewards()

        fun onClickAddTimer()

        fun onTimerSwiped(timer: Timer)

        fun definitivelyRemoveTimer()

        fun onReorderFinished(timerList: List<Timer>)

        fun cancelTimerDeletion()
        fun isInstantApp(): Boolean
    }
}