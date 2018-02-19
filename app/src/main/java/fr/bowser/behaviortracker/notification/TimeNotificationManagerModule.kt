package fr.bowser.behaviortracker.notification

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import javax.inject.Singleton

@Module
class TimeNotificationManagerModule {

    @Singleton
    @Provides
    fun provideTimerNotificationManager(context: Context, timeManager: TimeManager): TimerNotificationManager {
        return TimerNotificationManager(context, timeManager)
    }

}