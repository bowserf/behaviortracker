package fr.bowser.behaviortracker.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.alarm.AlarmTimerManagerModule
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.database.DatabaseManagerModule
import fr.bowser.behaviortracker.do_not_disturbed.DoNotDisturbModule
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.event.EventManagerModule
import fr.bowser.behaviortracker.inapp.InAppConfiguration
import fr.bowser.behaviortracker.inapp.InAppManagerModule
import fr.bowser.behaviortracker.notification_manager.NotificationManager
import fr.bowser.behaviortracker.notification_manager.NotificationManagerModule
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManagerModule
import fr.bowser.behaviortracker.review.ReviewModule
import fr.bowser.behaviortracker.review.ReviewStorage
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.setting.SettingManagerModule
import fr.bowser.behaviortracker.string.StringManagerModule
import fr.bowser.behaviortracker.time.TimeProvider
import fr.bowser.behaviortracker.time.TimeProviderModule
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimeManagerModule
import fr.bowser.behaviortracker.timer.TimerDAO
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.timer_list.TimerListManagerModule
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature.billing.InAppManager
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager
import fr.bowser.feature_review.ReviewManager
import fr.bowser.feature_string.StringManager
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AlarmTimerManagerModule::class,
        DatabaseManagerModule::class,
        DoNotDisturbModule::class,
        EventManagerModule::class,
        InAppManagerModule::class,
        NotificationManagerModule::class,
        PomodoroManagerModule::class,
        ReviewModule::class,
        SettingManagerModule::class,
        StringManagerModule::class,
        TimerListManagerModule::class,
        TimeManagerModule::class,
        TimeProviderModule::class
    )
)
interface BehaviorTrackerAppComponent {

    fun provideAlarmTimerManager(): AlarmTimerManager

    fun provideContext(): Context

    fun provideDatabaseManager(): DatabaseManager

    fun provideDoNotDisturbModule(): DoNotDisturbManager

    fun provideEventManager(): EventManager

    fun provideInAppConfiguration(): InAppConfiguration

    fun provideInAppManager(): InAppManager

    fun provideNotificationManager(): NotificationManager

    fun providePomodoroManager(): PomodoroManager

    fun provideReviewManager(): ReviewManager

    fun provideReviewStorage(): ReviewStorage

    fun provideSettingManager(): SettingManager

    fun provideStringManager(): StringManager

    fun provideTimeManager(): TimeManager

    fun provideTimeProvider(): TimeProvider

    fun provideTimerDAO(): TimerDAO

    fun provideTimerListManager(): TimerListManager

    @Component.Builder
    interface Builder {

        fun build(): BehaviorTrackerAppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }
}
