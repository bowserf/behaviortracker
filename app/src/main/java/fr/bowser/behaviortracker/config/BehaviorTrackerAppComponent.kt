package fr.bowser.behaviortracker.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.database.DatabaseManagerModule
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimeManagerModule
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerListManagerModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DatabaseManagerModule::class,
        TimeManagerModule::class,
        TimerListManagerModule::class))
interface BehaviorTrackerAppComponent {

    fun provideDatabaseManager(): DatabaseManager

    fun provideTimeManager(): TimeManager

    fun provideTimerListManager(): TimerListManager

    @Component.Builder
    interface Builder {

        fun build(): BehaviorTrackerAppComponent
        @BindsInstance
        fun context(context: Context): Builder
    }

}