package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.Timer

interface TimerContract {

    interface Screen {

        fun displayCreateTimerView()

        fun displayTimerListSections(sections: List<TimerListSection>)

        fun displayEmptyListView()

        fun displayListView()

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

        fun isInstantApp(): Boolean
    }
}