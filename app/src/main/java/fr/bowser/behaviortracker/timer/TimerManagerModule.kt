package fr.bowser.behaviortracker.timer

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TimerManagerModule {

    @Singleton
    @Provides
    fun provideTimerManager(): TimerManager {
        return TimerManager()
    }

}