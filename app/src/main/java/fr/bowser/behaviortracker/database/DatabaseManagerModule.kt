package fr.bowser.behaviortracker.database

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerDAO
import javax.inject.Singleton

@Module
class DatabaseManagerModule {

    @Singleton
    @Provides
    fun provideDatabaseManager(context: Context): DatabaseManager {
        return DatabaseManager(context)
    }

    @Singleton
    @Provides
    fun provideTimerDao(database: DatabaseManager): TimerDAO {
        return database.provideTimerDAO()
    }

}