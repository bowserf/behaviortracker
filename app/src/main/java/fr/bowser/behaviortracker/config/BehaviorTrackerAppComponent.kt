package fr.bowser.behaviortracker.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.alarm.AlarmStorageManager
import fr.bowser.behaviortracker.alarm.AlarmTimerManager
import fr.bowser.behaviortracker.alarm.AlarmTimerManagerModule
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.database.DatabaseManagerModule
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.event.EventManagerModule
import fr.bowser.behaviortracker.inapp.InAppManager
import fr.bowser.behaviortracker.inapp.InAppManagerModule
import fr.bowser.behaviortracker.inapp.InAppRepository
import fr.bowser.behaviortracker.notification.TimeNotificationManagerModule
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManagerModule
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.setting.SettingManagerModule
import fr.bowser.behaviortracker.timer.*
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
        TimerListManagerModule::class
    )
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

    fun provideAlarmStorageManager(): AlarmStorageManager

    fun provideInAppManager(): InAppManager

    fun provideInAppRepository(): InAppRepository

    fun providePomodoroManager(): PomodoroManager

    @Component.Builder
    interface Builder {

        fun build(): BehaviorTrackerAppComponent
        @BindsInstance
        fun context(context: Context): Builder
    }
}