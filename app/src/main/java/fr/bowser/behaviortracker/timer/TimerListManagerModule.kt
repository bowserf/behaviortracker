package fr.bowser.behaviortracker.timer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.database.DatabaseManager
import javax.inject.Singleton

@Module
class TimerListManagerModule {

    @Singleton
    @Provides
    fun provideTimerListManager(databaseManager: DatabaseManager): TimerListManager {
        return TimerListManager(databaseManager.provideTimerDAO())
    }

}