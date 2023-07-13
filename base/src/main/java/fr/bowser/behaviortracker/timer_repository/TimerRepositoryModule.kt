package fr.bowser.behaviortracker.timer_repository

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerDAO
import fr.bowser.behaviortracker.timer.TimerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class TimerRepositoryModule {

    @Singleton
    @Provides
    fun provideTimerRepository(timerDAO: TimerDAO, timeManager: TimerManager): TimerRepository {
        return TimerRepositoryImpl(
            coroutineScope = CoroutineScope(Dispatchers.IO),
            timerDAO = timerDAO,
            timeManager = timeManager
        )
    }
}
