package fr.bowser.behaviortracker.timer

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TimeManagerModule {

    @Singleton
    @Provides
    fun provideTimeManager(): TimeManager {
        return TimeManager()
    }

}