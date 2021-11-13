package fr.bowser.behaviortracker.timer

import android.content.Context
import android.os.Handler
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.notification.TimeService
import fr.bowser.behaviortracker.time.TimeProvider
import javax.inject.Singleton

@Module
class TimeManagerModule {

    @Singleton
    @Provides
    fun provideTimeManager(
        context: Context,
        timerDAO: TimerDAO,
        timeProvider: TimeProvider
    ): TimeManager {
        return TimeManagerImpl(
            timerDAO,
            timeProvider,
            Handler(),
            createAddOn(context)
        )
    }

    private fun createAddOn(context: Context) = object : TimeManagerImpl.AddOn {
        override fun onTimerStarted() {
            TimeService.start(context)
        }
    }
}