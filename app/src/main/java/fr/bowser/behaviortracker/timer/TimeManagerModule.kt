package fr.bowser.behaviortracker.timer

import android.os.Handler
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.database.DatabaseManager
import javax.inject.Singleton

@Module
class TimeManagerModule {

    @Singleton
    @Provides
    fun provideTimeManager(databaseManager: DatabaseManager): TimeManager {
        return TimeManagerImpl(databaseManager.provideTimerDAO(), Handler())
    }

}