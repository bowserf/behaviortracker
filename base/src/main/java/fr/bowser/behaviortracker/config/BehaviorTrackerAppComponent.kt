package fr.bowser.behaviortracker.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.alarm.AlarmTimerManagerModule
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.database.DatabaseManagerModule
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager
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
import fr.bowser.behaviortracker.timer.*
import fr.bowser.feature.alarm.AlarmTimerManager
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        DatabaseManagerModule::class,
        TimeManagerModule::class,
        TimeNotificationManagerModule::class,
        SettingManagerModule::class,
        EventManagerModule::class,
        AlarmTimerManagerModule::class,
        InAppManagerModule::class,
        PomodoroManagerModule::class,
        TimerListManagerModule::class,
        DoNotDisturbModule::class
    ), dependencies = [
        InstantAppComponent::class
    ]
)
interface BehaviorTrackerAppComponent {

    fun provideContext(): Context

    fun provideDatabaseManager(): DatabaseManager

    fun provideTimerDAO(): TimerDAO

    fun provideTimeManager(): TimeManager

    fun provideTimerListManager(): TimerListManager

    fun provideTimerNotificationManager(): TimerNotificationManager

    fun provideSettingManager(): SettingManager

    fun provideEventManager(): EventManager

    fun provideAlarmTimerManager(): AlarmTimerManager

    fun provideInAppManager(): InAppManager

    fun provideInAppRepository(): InAppRepository

    fun providePomodoroManager(): PomodoroManager

    fun provideMyInstantApp(): InstantAppManager

    fun provideDoNotDisturbModule(): DoNotDisturbManager

    @Component.Builder
    interface Builder {

        fun build(): BehaviorTrackerAppComponent
        @BindsInstance
        fun context(context: Context): Builder

        fun myInstantApp(instantAppComponent: InstantAppComponent): Builder
    }
}