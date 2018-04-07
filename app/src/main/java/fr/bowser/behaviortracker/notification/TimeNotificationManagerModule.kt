package fr.bowser.behaviortracker.notification

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import javax.inject.Singleton

@Module
class TimeNotificationManagerModule {

    @Singleton
    @Provides
    fun provideTimerNotificationManager(context: Context,
                                        timeManager: TimeManager,
                                        timerListManager: TimerListManager)
            : TimerNotificationManager {
        return TimerNotificationManager(context, timeManager, timerListManager)
    }

}