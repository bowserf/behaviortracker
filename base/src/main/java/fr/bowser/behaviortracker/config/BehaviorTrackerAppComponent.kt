package fr.bowser.behaviortracker.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.alarm.AlarmTimerManagerModule
import fr.bowser.behaviortracker.alarm_notification.AlarmNotificationManager
import fr.bowser.behaviortracker.alarm_notification.AlarmNotificationModule
import fr.bowser.behaviortracker.app_initialization.AppInitialization
import fr.bowser.behaviortracker.app_initialization.AppInitializationModule
import fr.bowser.behaviortracker.clipboard.CopyDataToClipboardModule
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.database.DatabaseManagerModule
import fr.bowser.behaviortracker.do_not_disturbed.DoNotDisturbModule
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.event.EventManagerModule
import fr.bowser.behaviortracker.inapp.InAppConfiguration
import fr.bowser.behaviortracker.inapp.InAppManagerModule
import fr.bowser.behaviortracker.navigation.NavigationManager
import fr.bowser.behaviortracker.navigation.NavigationModule
import fr.bowser.behaviortracker.notification_manager.NotificationManager
import fr.bowser.behaviortracker.notification_manager.NotificationManagerModule
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManagerModule
import fr.bowser.behaviortracker.review.ReviewModule
import fr.bowser.behaviortracker.review.ReviewStorage
import fr.bowser.behaviortracker.scroll_to_timer_manager.ScrollToTimerManager
import fr.bowser.behaviortracker.scroll_to_timer_manager.ScrollToTimerManagerModule
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.setting.SettingManagerModule
import fr.bowser.behaviortracker.string.StringManagerModule
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.time_provider.TimeProviderModule
import fr.bowser.behaviortracker.timer.TimerDAO
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer.TimerManagerModule
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.timer_repository.TimerRepositoryModule
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature.billing.InAppManager
import fr.bowser.feature_clipboard.CopyDataToClipboardManager
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager
import fr.bowser.feature_review.ReviewManager
import fr.bowser.feature_string.StringManager
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AppInitializationModule::class,
        AlarmNotificationModule::class,
        AlarmTimerManagerModule::class,
        CopyDataToClipboardModule::class,
        DatabaseManagerModule::class,
        DoNotDisturbModule::class,
        EventManagerModule::class,
        InAppManagerModule::class,
        NavigationModule::class,
        NotificationManagerModule::class,
        PomodoroManagerModule::class,
        ReviewModule::class,
        ScrollToTimerManagerModule::class,
        SettingManagerModule::class,
        StringManagerModule::class,
        TimerRepositoryModule::class,
        TimerManagerModule::class,
        TimeProviderModule::class
    ),
)
interface BehaviorTrackerAppComponent {

    fun provideAlarmNotificationManager(): AlarmNotificationManager

    fun provideAlarmTimerManager(): AlarmTimerManager

    fun provideAppInitialization(): AppInitialization

    fun provideContext(): Context

    fun provideCopyDataToClipboardManager(): CopyDataToClipboardManager

    fun provideDatabaseManager(): DatabaseManager

    fun provideDoNotDisturbModule(): DoNotDisturbManager

    fun provideEventManager(): EventManager

    fun provideInAppConfiguration(): InAppConfiguration

    fun provideInAppManager(): InAppManager

    fun provideNavigationManager(): NavigationManager

    fun provideNotificationManager(): NotificationManager

    fun providePomodoroManager(): PomodoroManager

    fun provideReviewManager(): ReviewManager

    fun provideReviewStorage(): ReviewStorage

    fun provideScrollToTimerManager(): ScrollToTimerManager

    fun provideSettingManager(): SettingManager

    fun provideStringManager(): StringManager

    fun provideTimeManager(): TimerManager

    fun provideTimeProvider(): TimeProvider

    fun provideTimerDAO(): TimerDAO

    fun provideTimerRepositoryManager(): TimerRepository

    @Component.Builder
    interface Builder {

        fun build(): BehaviorTrackerAppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }
}
