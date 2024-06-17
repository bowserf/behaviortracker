package fr.bowser.behaviortracker.timer

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.timer_service.TimerService
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module
class TimerManagerModule {

    @Singleton
    @Provides
    fun provideTimeManager(
        context: Context,
        timerDAO: TimerDAO,
        timeProvider: TimeProvider,
    ): TimerManager {
        return TimerManagerImpl(
            coroutineScope = CoroutineScope(Dispatchers.IO),
            timerDAO = timerDAO,
            timeProvider = timeProvider,
            addOn = createAddOn(context),
        )
    }

    private fun createAddOn(context: Context) = object : TimerManagerImpl.AddOn {
        override fun onTimerStarted() {
            TimerService.start(context)
        }
    }
}
