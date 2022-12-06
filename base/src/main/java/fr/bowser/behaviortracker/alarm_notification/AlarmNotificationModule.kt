package fr.bowser.behaviortracker.alarm_notification

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import javax.inject.Singleton

@Module
class AlarmNotificationModule {

    @Singleton
    @Provides
    fun provideAlarmNotificationManager(
        context: Context,
        eventManager: EventManager
    ): AlarmNotificationManager {
        return AlarmNotificationManagerImpl(context, eventManager)
    }
}
