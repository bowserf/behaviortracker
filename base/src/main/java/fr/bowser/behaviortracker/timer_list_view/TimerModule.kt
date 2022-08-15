package fr.bowser.behaviortracker.timer_list_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.notification_manager.NotificationManager
import fr.bowser.behaviortracker.review.ReviewStorage
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature_review.ReviewManager
import fr.bowser.feature_string.StringManager

@Module
class TimerModule(private val timerScreen: TimerContract.Screen) {

    @GenericScope(component = TimerComponent::class)
    @Provides
    fun provideTimerPresenter(
        alarmTimerManager: AlarmTimerManager,
        instantAppManager: InstantAppManager,
        notificationManager: NotificationManager,
        reviewManager: ReviewManager,
        reviewStorage: ReviewStorage,
        stringManager: StringManager,
        timerListManager: TimerListManager,
        timeManager: TimeManager,
    ): TimerContract.Presenter {
        return TimerPresenter(
            timerScreen,
            alarmTimerManager,
            notificationManager,
            reviewManager,
            reviewStorage,
            stringManager,
            timerListManager,
            timeManager,
            instantAppManager.isInstantApp()
        )
    }
}
