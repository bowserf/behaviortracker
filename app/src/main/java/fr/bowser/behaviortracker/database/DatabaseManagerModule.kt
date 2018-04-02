package fr.bowser.behaviortracker.database

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseManagerModule {

    @Singleton
    @Provides
    fun provideDatabaseManager(context: Context): DatabaseManager {
        return DatabaseManager(context)
    }

}