package fr.bowser.behaviortracker.timer_list_view

import fr.bowser.behaviortracker.explain_permission_request.ExplainPermissionRequestModel

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

        fun onNotificationPermissionAlreadyGranted()

        fun onNotificationPermissionGranted()

        fun onNotificationPermissionDeclined()

        fun shouldShowNotificationRequestPermissionRationale(permission: String)
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

        fun displayExplainNotificationPermission(explainPermissionRequestModel: ExplainPermissionRequestModel)

        fun displayAskScheduleAlarmPermission()

        fun displayAskNotificationDisplay()

        fun checkNotificationPermission(permission: String)

        fun updateTotalTime(totalTime: Long)
    }
}
