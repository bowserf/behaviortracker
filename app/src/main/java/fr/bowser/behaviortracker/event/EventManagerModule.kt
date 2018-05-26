package fr.bowser.behaviortracker.event

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EventManagerModule() {

    @Singleton
    @Provides
    fun provideEventManager(context: Context): EventManager {
        return EventManagerImpl(context)
    }

}