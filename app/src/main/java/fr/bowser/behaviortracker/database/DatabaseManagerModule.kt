package fr.bowser.behaviortracker.database

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.timer.TimerDAO
import fr.bowser.behaviortracker.timer.TimerDaoUA
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
    fun provideTimerDao(context: Context, database: DatabaseManager): TimerDAO {
        return if(BuildConfig.UA) {
            TimerDaoUA(context)
        }else{
            database.provideTimerDAO()
        }
    }

}