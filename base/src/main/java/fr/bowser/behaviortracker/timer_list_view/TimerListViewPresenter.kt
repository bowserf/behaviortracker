package fr.bowser.behaviortracker.timer_list_view

import android.Manifest
import android.os.Build
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.explain_permission_request_view.ExplainPermissionRequestViewModel
import fr.bowser.behaviortracker.notification_manager.NotificationManager
import fr.bowser.behaviortracker.review.ReviewStorage
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature_review.ReviewActivityContainer
import fr.bowser.feature_review.ReviewManager
import fr.bowser.feature_string.StringManager

class TimerListViewPresenter(
    private val screen: TimerListViewContract.Screen,
    private val alarmTimerManager: AlarmTimerManager,
    private val notificationManager: NotificationManager,
    private val reviewManager: ReviewManager,
    private val reviewStorage: ReviewStorage,
    private val stringManager: StringManager,
    private val timeManager: TimerManager,
    private val timerListManager: TimerListManager,
    private val isInstantApp: Boolean
) : TimerListViewContract.Presenter {

    private var ongoingDeletionTimer: Timer? = null

    private val reviewManagerListener = createReviewManagerListener()

    private val timerListManagerListener = createTimerListManagerListener()

    private val timeManagerListener = createTimeManagerListener()

    override fun onStart() {
        reviewManager.addListener(reviewManagerListener)
        timerListManager.addListener(timerListManagerListener)
        timeManager.addListener(timeManagerListener)

        updateTimerList()
        updateListVisibility()
        updateTotalTimerTime()
    }

    override fun onStop() {
        reviewManager.removeListener(reviewManagerListener)
        timerListManager.removeListener(timerListManagerListener)
        timeManager.removeListener(timeManagerListener)
    }

    override fun onClickResetAll() {
        screen.displayResetAllDialog()
    }

    override fun onClickResetAllTimers() {
        timeManager.stopTimer()
        val timers = timerListManager.getTimerList()
        timers.forEach { timer ->
            timeManager.updateTime(timer, 0f)
        }
    }

    override fun onClickSettings() {
        screen.displaySettingsView()
    }

    override fun onClickRemoveAllTimers() {
        screen.displayRemoveAllTimersConfirmationDialog()
    }

    override fun onClickConfirmRemoveAllTimers() {
        timerListManager.removeAllTimers()
    }

    override fun onClickAlarm() {
        if (alarmTimerManager.canScheduleAlarm()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                screen.checkNotificationPermission(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                displayAlarmTimerDialogIfNotificationsAreEnabled()
            }
        } else {
            screen.displayAskScheduleAlarmPermission()
        }
    }

    override fun onNotificationPermissionAlreadyGranted() {
        displayAlarmTimerDialogIfNotificationsAreEnabled()
    }

    override fun shouldShowNotificationRequestPermissionRationale(permission: String) {
        val explainPermissionRequestModel = ExplainPermissionRequestViewModel(
            stringManager.getString(R.string.timer_list_explain_notification_permission_title),
            stringManager.getString(R.string.timer_list_explain_notification_permission_message),
            R.drawable.explain_permission_request_view_notification_permission,
            listOf(permission)
        )
        screen.displayExplainNotificationPermission(explainPermissionRequestModel)
    }

    override fun onClickRateApp(activityContainer: ReviewActivityContainer) {
        reviewManager.launchReviewFlow(activityContainer)
    }

    override fun onNotificationPermissionGranted() {
        displayAlarmTimerDialogIfNotificationsAreEnabled()
    }

    override fun onNotificationPermissionDeclined() {
        // nothing to do
    }

    override fun onClickAskNotificationDisplaySettings() {
        notificationManager.displayNotificationSettings()
    }

    override fun onClickAskScheduleAlarmSettings() {
        alarmTimerManager.askScheduleAlarmPermissionIfNeeded()
    }

    override fun onClickRewards() {
        screen.displayRewardsView()
    }

    override fun onClickAddTimer() {
        screen.displayCreateTimerView()
    }

    override fun isInstantApp(): Boolean {
        return isInstantApp
    }

    override fun isReviewAlreadyDone(): Boolean {
        return reviewStorage.isReviewMarked()
    }

    override fun onTimerSwiped(timerPosition: Int) {
        ongoingDeletionTimer = timerListManager.getTimerList().first {
            it.position == timerPosition
        }
        timerListManager.removeTimer(ongoingDeletionTimer!!)
        screen.displayCancelDeletionView(CANCEL_TIMER_REMOVAL_DURATION)
        updateTimerList()
    }

    override fun onClickCancelTimerDeletion() {
        val restoreTimer = ongoingDeletionTimer ?: return
        ongoingDeletionTimer = null
        timerListManager.addTimer(restoreTimer)
    }

    private fun updateListVisibility() {
        val timerList = timerListManager.getTimerList()
        if (timerList.isEmpty()) {
            screen.displayEmptyListView()
        } else {
            screen.displayListView()
        }
    }

    private fun updateTotalTimerTime() {
        var totalTime = 0f
        timerListManager.getTimerList().forEach { totalTime += it.time }
        screen.updateTotalTime(totalTime.toLong())
    }

    private fun displayAlarmTimerDialogIfNotificationsAreEnabled() {
        if (notificationManager.areNotificationsEnabled()) {
            screen.displayAlarmTimerDialog()
        } else {
            screen.displayAskNotificationDisplay()
        }
    }

    private fun createReviewManagerListener() = object : ReviewManager.Listener {
        override fun onSucceeded() {
            screen.invalidateMenu()
            reviewStorage.markReview()
        }

        override fun onFailed(failReason: ReviewManager.FailReason) {
            // nothing to do
        }
    }

    private fun updateTimerList() {
        val timers = timerListManager.getTimerList().filter {
            it != ongoingDeletionTimer
        }
        screen.displayTimers(timers)
    }

    private fun createTimeManagerListener() = object : TimerManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            // nothing to do
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            updateTotalTimerTime()
        }
    }

    private fun createTimerListManagerListener() = object : TimerListManager.Listener {
        override fun onTimerRemoved(removedTimer: Timer) {
            updateListVisibility()
            updateTotalTimerTime()
        }

        override fun onTimerAdded(updatedTimer: Timer) {
            updateTimerList()
            updateListVisibility()
            updateTotalTimerTime()
        }

        override fun onTimerRenamed(updatedTimer: Timer) {
            // nothing to do
        }
    }

    companion object {
        private const val CANCEL_TIMER_REMOVAL_DURATION = 3_000
    }
}
