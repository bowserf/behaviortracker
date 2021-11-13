package fr.bowser.behaviortracker.timer

import android.os.Handler
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.time.TimeProvider
import javax.inject.Singleton

@Module
class TimeManagerModule {

    @Singleton
    @Provides
    fun provideTimeManager(
        timerDAO: TimerDAO,
        timeProvider: TimeProvider
    ): TimeManager {
        return TimeManagerImpl(
            timerDAO,
            timeProvider,
            Handler()
        )
    }
}