package fr.bowser.behaviortracker.timer

import android.os.Handler
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.setting.SettingManager
import javax.inject.Singleton

@Module
class TimeManagerModule {

    @Singleton
    @Provides
    fun provideTimeManager(databaseManager: DatabaseManager, settingManager: SettingManager): TimeManager {
        return TimeManagerImpl(databaseManager.provideTimerDAO(), settingManager, Handler())
    }

}