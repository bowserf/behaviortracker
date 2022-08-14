package fr.bowser.behaviortracker.notification_manager

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationManagerModule {

    @Singleton
    @Provides
    fun provideNotificationManager(context: Context): NotificationManager {
        return NotificationManagerImpl(context)
    }
}
