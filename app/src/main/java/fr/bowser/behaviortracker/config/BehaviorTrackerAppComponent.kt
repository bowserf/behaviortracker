package fr.bowser.behaviortracker.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.database.DatabaseManagerModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DatabaseManagerModule::class))
interface BehaviorTrackerAppComponent {

    fun provideDatabaseManager(): DatabaseManager

    @Component.Builder
    interface Builder {

        fun build(): BehaviorTrackerAppComponent
        @BindsInstance
        fun context(context: Context): Builder
    }

}