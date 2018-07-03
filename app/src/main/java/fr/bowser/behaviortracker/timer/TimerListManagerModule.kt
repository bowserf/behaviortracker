package fr.bowser.behaviortracker.timer

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TimerListManagerModule {

    @Singleton
    @Provides
    fun provideTimerListManager(timerDAO: TimerDAO, timeManager: TimeManager): TimerListManager {
        return TimerListManagerImpl(timerDAO, timeManager)
    }

}