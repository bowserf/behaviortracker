package fr.bowser.behaviortracker.timer_list_view

interface TimerContract {

    interface Presenter {

        fun init()

        fun onStart()

        fun onStop()

        fun onClickResetAll()

        fun onClickResetAllTimers()

        fun onClickSettings()

        fun onClickRemoveAllTimers()

        fun onClickConfirmRemoveAllTimers()

        fun onClickAlarm()

        fun onClickRewards()

        fun onClickAddTimer()

        fun isInstantApp(): Boolean

        fun onClickAskScheduleAlarmSettings()

        fun onClickAskNotificationDisplaySettings()
    }

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

        fun updateTotalTime(totalTime: Long)

        fun displayAskScheduleAlarmPermission()

        fun displayAskNotificationDisplay()
    }
}
