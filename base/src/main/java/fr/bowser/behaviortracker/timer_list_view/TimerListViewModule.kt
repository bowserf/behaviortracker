package fr.bowser.behaviortracker.timer_list_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.notification_manager.NotificationManager
import fr.bowser.behaviortracker.review.ReviewStorage
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature_clipboard.CopyDataToClipboardManager
import fr.bowser.feature_review.ReviewManager
import fr.bowser.feature_string.StringManager

@Module
class TimerListViewModule(private val timerScreen: TimerListViewContract.Screen) {

    @GenericScope(component = TimerListViewComponent::class)
    @Provides
    fun provideTimerPresenter(
        alarmTimerManager: AlarmTimerManager,
        copyDataToClipboardManager: CopyDataToClipboardManager,
        instantAppManager: InstantAppManager,
        notificationManager: NotificationManager,
        reviewManager: ReviewManager,
        reviewStorage: ReviewStorage,
        stringManager: StringManager,
        timerListManager: TimerListManager,
        timeManager: TimerManager
    ): TimerListViewContract.Presenter {
        return TimerListViewPresenter(
            timerScreen,
            alarmTimerManager,
            copyDataToClipboardManager,
            notificationManager,
            reviewManager,
            reviewStorage,
            stringManager,
            timeManager,
            timerListManager,
            instantAppManager.isInstantApp()
        )
    }
}
