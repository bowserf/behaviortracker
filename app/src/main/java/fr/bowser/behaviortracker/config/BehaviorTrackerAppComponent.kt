package fr.bowser.behaviortracker.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.database.DatabaseManagerModule
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer.TimerManagerModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DatabaseManagerModule::class, TimerManagerModule::class))
interface BehaviorTrackerAppComponent {

    fun provideDatabaseManager(): DatabaseManager

    fun provideTimerManager(): TimerManager

    @Component.Builder
    interface Builder {

        fun build(): BehaviorTrackerAppComponent
        @BindsInstance
        fun context(context: Context): Builder
    }

}