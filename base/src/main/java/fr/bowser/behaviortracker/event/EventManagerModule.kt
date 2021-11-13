package fr.bowser.behaviortracker.event

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import javax.inject.Singleton

@Module
class EventManagerModule {

    @Singleton
    @Provides
    fun provideEventManager(context: Context, instantAppManager: InstantAppManager): EventManager {
        return EventManagerImpl(context, instantAppManager)
    }
}
