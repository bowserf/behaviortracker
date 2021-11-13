package fr.bowser.behaviortracker.timer_list

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerDAO
import javax.inject.Singleton

@Module
class TimerListManagerModule {

    @Singleton
    @Provides
    fun provideTimerListManager(timerDAO: TimerDAO, timeManager: TimeManager): TimerListManager {
        return TimerListManagerImpl(timerDAO, timeManager)
    }
}
