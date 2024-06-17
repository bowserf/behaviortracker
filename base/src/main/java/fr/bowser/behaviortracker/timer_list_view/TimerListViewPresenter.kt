package fr.bowser.behaviortracker.timer_list_view

import android.Manifest
import android.os.Build
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.explain_permission_request_view.ExplainPermissionRequestViewModel
import fr.bowser.behaviortracker.interrupt_timer.CreateInterruptTimerUseCase
import fr.bowser.behaviortracker.notification_manager.NotificationManager
import fr.bowser.behaviortracker.review.ReviewStorage
import fr.bowser.behaviortracker.scroll_to_timer_manager.ScrollToTimerManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.TimeConverter
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature_clipboard.CopyDataToClipboardManager
import fr.bowser.feature_review.ReviewActivityContainer
import fr.bowser.feature_review.ReviewManager
import fr.bowser.feature_string.StringManager
import java.util.Collections
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerListViewPresenter(
    private val screen: TimerListViewContract.Screen,
    private val alarmTimerManager: AlarmTimerManager,
    private val createInterruptTimerUseCase: CreateInterruptTimerUseCase,
    private val copyDataToClipboardManager: CopyDataToClipboardManager,
    private val notificationManager: NotificationManager,
    private val reviewManager: ReviewManager,
    private val reviewStorage: ReviewStorage,
    private val scrollToTimerManager: ScrollToTimerManager,
    private val stringManager: StringManager,
    private val timeManager: TimerManager,
    private val timerRepository: TimerRepository,
    coroutineContext: CoroutineContext = Dispatchers.Main,
) : TimerListViewContract.Presenter {

    private var coroutineScope = CoroutineScope(coroutineContext)

    private var ongoingDeletionTimer: Timer? = null

    private val reviewManagerListener = createReviewManagerListener()

    private val scrollToTimerListener = createScrollToTimerListener()

    private val timerRepositoryListener = createTimerRepositoryListener()

    private val timeManagerListener = createTimeManagerListener()

    override fun onStart() {
        reviewManager.addListener(reviewManagerListener)
        timerRepository.addListener(timerRepositoryListener)
        timeManager.addListener(timeManagerListener)
        scrollToTimerManager.addListener(scrollToTimerListener)

        updateTimerList()
        updateListVisibility()
        updateTotalTimerTime()
    }

    override fun onStop() {
        scrollToTimerManager.removeListener(scrollToTimerListener)
        reviewManager.removeListener(reviewManagerListener)
        timerRepository.removeListener(timerRepositoryListener)
        timeManager.removeListener(timeManagerListener)
    }

    override fun onClickResetAll() {
        screen.displayResetAllDialog()
    }

    override fun onClickResetAllTimers() {
        timeManager.stopTimer()
        val timers = timerRepository.getTimerList()
        timers.forEach { timer ->
            timeManager.resetTime(timer)
        }
    }

    override fun onClickExportTimers() {
        var export = ""
        var totalTime = 0f
        timerRepository.getTimerList().forEach {
            if (it.time > 0) {
                totalTime += it.time
                export += "${it.name}: ${
                    TimeConverter.convertSecondsToHumanTime(
                        it.time.toLong(),
                        TimeConverter.DisplayHoursMode.IfPossible,
                    )
                }\n"
            }
        }
        val totalTimeTitle = stringManager.getString(R.string.export_total_time)
        val totalTimeStr = TimeConverter.convertSecondsToHumanTime(
            totalTime.toLong(),
            TimeConverter.DisplayHoursMode.IfPossible,
        )
        export += "$totalTimeTitle: $totalTimeStr"
        copyDataToClipboardManager.copy(export)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            screen.displayExportSucceeded()
        }
    }

    override fun onClickSettings() {
        screen.displaySettingsView()
    }

    override fun onClickRemoveAllTimers() {
        screen.displayRemoveAllTimersConfirmationDialog()
    }

    override fun onClickConfirmRemoveAllTimers() {
        timerRepository.removeAllTimers()
    }

    override fun onClickAlarm() {
        if (alarmTimerManager.canScheduleAlarm()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                screen.checkNotificationPermissionForAlarm()
            } else {
                displayAlarmTimerDialogIfNotificationsAreEnabled()
            }
        } else {
            screen.displayAskScheduleAlarmPermission()
        }
    }

    override fun onNotificationPermissionAlreadyGrantedForAlarm() {
        displayAlarmTimerDialogIfNotificationsAreEnabled()
    }

    override fun shouldShowNotificationRequestPermissionRationaleForAlarm() {
        val explainPermissionRequestModel = ExplainPermissionRequestViewModel(
            stringManager.getString(R.string.timer_list_explain_notification_permission_title),
            stringManager.getString(R.string.timer_list_explain_notification_permission_message),
            R.drawable.explain_permission_request_view_notification_permission,
            listOf(Manifest.permission.POST_NOTIFICATIONS),
        )
        screen.displayExplainNotificationPermissionForAlarm(explainPermissionRequestModel)
    }

    override fun onTimerPositionChanged(fromPosition: Int, toPosition: Int) {
        updateTimersOrder(fromPosition, toPosition)

        screen.reorderTimer(fromPosition, toPosition)
    }

    override fun onClickInterruptTimer() {
        createInterruptTimerUseCase()
    }

    override fun onClickRateApp(activityContainer: ReviewActivityContainer) {
        reviewManager.launchReviewFlow(activityContainer)
    }

    override fun onNotificationPermissionGranted() {
        displayAlarmTimerDialogIfNotificationsAreEnabled()
    }

    override fun onNotificationPermissionDeclined() {
        displayAlarmTimerDialogIfNotificationsAreEnabled()
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

    override fun isReviewAlreadyDone(): Boolean {
        return reviewStorage.isReviewMarked()
    }

    override fun onTimerSwiped(timerPosition: Int) {
        ongoingDeletionTimer = timerRepository.getTimerList().first {
            it.position == timerPosition
        }
        timerRepository.removeTimer(ongoingDeletionTimer!!)
        screen.displayCancelDeletionView(CANCEL_TIMER_REMOVAL_DURATION)
        updateTimerList()
    }

    override fun onClickCancelTimerDeletion() {
        val restoreTimer = ongoingDeletionTimer ?: return
        ongoingDeletionTimer = null
        timerRepository.addTimer(restoreTimer)
    }

    private fun updateListVisibility() {
        val timerList = timerRepository.getTimerList()
        if (timerList.isEmpty()) {
            screen.displayEmptyListView()
        } else {
            screen.displayListView()
        }
    }

    private fun updateTotalTimerTime() {
        var totalTime = 0f
        timerRepository.getTimerList().forEach { totalTime += it.time }
        screen.updateTotalTime(totalTime.toLong())
    }

    private fun displayAlarmTimerDialogIfNotificationsAreEnabled() {
        if (notificationManager.areNotificationsEnabled()) {
            screen.displayAlarmTimerDialog()
        } else {
            screen.displayAskNotificationDisplay()
        }
    }

    private fun updateTimerList() {
        val timers = timerRepository.getTimerList().filter {
            it != ongoingDeletionTimer
        }
        screen.displayTimers(timers)
    }

    private fun updateTimersOrder(fromPosition: Int, toPosition: Int) {
        val timers = timerRepository.getTimerList()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(timers, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(timers, i, i - 1)
            }
        }
        timerRepository.reorderTimerList(timers)
    }

    private fun scrollToTimerInternal(timerId: Long) {
        val index = timerRepository.getTimerList().indexOfFirst { it.id == timerId }
        screen.scrollToTimer(index)
    }

    private fun askNotificationPermissionIfNeeded() {
        if (!notificationManager.areNotificationsEnabled() &&
            !screen.shouldShowNotificationPermissionRationale()
        ) {
            screen.displayAskNotificationPermissionForManagingTimers()
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

    private fun createScrollToTimerListener() = object : ScrollToTimerManager.Listener {
        override fun scrollToTimer(timerId: Long) {
            scrollToTimerInternal(timerId)
        }
    }

    private fun createTimeManagerListener() = object : TimerManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            // nothing to do
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            updateTotalTimerTime()
        }
    }

    private fun createTimerRepositoryListener() = object : TimerRepository.Listener {
        override fun onTimerRemoved(removedTimer: Timer) {
            updateListVisibility()
            updateTotalTimerTime()
        }

        override fun onTimerAdded(updatedTimer: Timer) {
            updateTimerList()
            updateListVisibility()
            updateTotalTimerTime()
            coroutineScope.launch {
                delay(200)
                scrollToTimerInternal(updatedTimer.id)
            }

            askNotificationPermissionIfNeeded()
        }

        override fun onTimerRenamed(updatedTimer: Timer) {
            // nothing to do
        }
    }

    companion object {
        private const val CANCEL_TIMER_REMOVAL_DURATION = 3_000
    }
}
