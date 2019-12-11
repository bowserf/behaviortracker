package fr.bowser.behaviortracker.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.alarm.AlarmTimerManagerModule
import fr.bowser.behaviortracker.app_initialization.AppInitialization
import fr.bowser.behaviortracker.app_initialization.AppInitializationComponent
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.database.DatabaseManagerModule
import fr.bowser.behaviortracker.do_not_disturbed.DoNotDisturbModule
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.event.EventManagerModule
import fr.bowser.behaviortracker.inapp.InAppManager
import fr.bowser.behaviortracker.inapp.InAppManagerModule
import fr.bowser.behaviortracker.inapp.InAppRepository
import fr.bowser.behaviortracker.instantapp.InstantAppComponent
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.notification.TimeNotificationManagerModule
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManagerModule
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.setting.SettingManagerModule
import fr.bowser.behaviortracker.string.StringManagerModule
import fr.bowser.behaviortracker.time.TimeProvider
import fr.bowser.behaviortracker.time.TimeProviderModule
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimeManagerModule
import fr.bowser.behaviortracker.timer.TimerDAO
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerListManagerModule
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager
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
        PomodoroManagerModule::class,
        TimeManagerModule::class,
        TimeNotificationManagerModule::class,
        TimeProviderModule::class,
        TimerListManagerModule::class,
        SettingManagerModule::class,
        StringManagerModule::class,
    ), dependencies = [
        InstantAppComponent::class,
        AppInitializationComponent::class
    ]
)
interface BehaviorTrackerAppComponent {

    fun provideAlarmTimerManager(): AlarmTimerManager

    fun provideContext(): Context

    fun provideDatabaseManager(): DatabaseManager

    fun provideDoNotDisturbModule(): DoNotDisturbManager

    fun provideEventManager(): EventManager

    fun provideInAppManager(): InAppManager

    fun provideInAppRepository(): InAppRepository

    fun provideMyInstantApp(): InstantAppManager

    fun provideTimeManager(): TimeManager

    fun provideTimeProvider(): TimeProvider

    fun providePomodoroManager(): PomodoroManager

    fun provideTimerDAO(): TimerDAO

    fun provideTimerListManager(): TimerListManager

    fun provideTimerNotificationManager(): TimerNotificationManager

    fun provideSettingManager(): SettingManager

    fun provideStringManager(): StringManager

    fun provideAppInitialization(): AppInitialization

    @Component.Builder
    interface Builder {

        fun build(): BehaviorTrackerAppComponent

        @BindsInstance
        fun context(context: Context): Builder

        fun myInstantApp(instantAppComponent: InstantAppComponent): Builder

        fun appInitialization(appInitializationComponent: AppInitializationComponent): Builder
    }
}