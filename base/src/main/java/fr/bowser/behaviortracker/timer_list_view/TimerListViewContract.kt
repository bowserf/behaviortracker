package fr.bowser.behaviortracker.timer_list_view

import fr.bowser.behaviortracker.explain_permission_request_view.ExplainPermissionRequestViewModel
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.feature_review.ReviewActivityContainer

interface TimerListViewContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun onClickRateApp(activityContainer: ReviewActivityContainer)

        fun onClickResetAll()

        fun onClickResetAllTimers()

        fun onClickExportTimers()

        fun onClickSettings()

        fun onClickRemoveAllTimers()

        fun onClickCancelTimerDeletion()

        fun onClickConfirmRemoveAllTimers()

        fun onClickAlarm()

        fun onClickRewards()

        fun onClickAddTimer()

        fun isReviewAlreadyDone(): Boolean

        fun onClickAskScheduleAlarmSettings()

        fun onClickAskNotificationDisplaySettings()

        fun onNotificationPermissionAlreadyGrantedForAlarm()

        fun onNotificationPermissionGranted()

        fun onNotificationPermissionDeclined()

        fun onTimerSwiped(timerPosition: Int)

        fun shouldShowNotificationRequestPermissionRationaleForAlarm()

        fun onTimerPositionChanged(fromPosition: Int, toPosition: Int)

        fun onClickInterruptTimer()
    }

    interface Screen {

        fun displayCreateTimerView()

        fun displayTimers(timers: List<Timer>)

        fun displayEmptyListView()

        fun displayListView()

        fun displayResetAllDialog()

        fun displaySettingsView()

        fun displayAlarmTimerDialog()

        fun displayRewardsView()

        fun displayRemoveAllTimersConfirmationDialog()

        fun displayExplainNotificationPermissionForAlarm(
            explainPermissionRequestModel: ExplainPermissionRequestViewModel,
        )

        fun displayAskScheduleAlarmPermission()

        fun displayAskNotificationDisplay()

        fun displayCancelDeletionView(cancelDuration: Int)

        fun checkNotificationPermissionForAlarm()

        fun shouldShowNotificationPermissionRationale(): Boolean

        fun updateTotalTime(totalTime: Long)

        fun invalidateMenu()

        fun displayExportSucceeded()

        fun scrollToTimer(timerIndex: Int)

        fun reorderTimer(fromPosition: Int, toPosition: Int)

        fun displayAskNotificationPermissionForManagingTimers()
    }
}
