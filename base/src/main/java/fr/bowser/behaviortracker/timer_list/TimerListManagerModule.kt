package fr.bowser.behaviortracker.timer_list

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerDAO
import fr.bowser.behaviortracker.timer.TimerManager
import javax.inject.Singleton

@Module
class TimerListManagerModule {

    @Singleton
    @Provides
    fun provideTimerListManager(timerDAO: TimerDAO, timeManager: TimerManager): TimerListManager {
        return TimerListManagerImpl(timerDAO, timeManager)
    }
}
