package fr.bowser.behaviortracker.app_initialization

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppInitializationModule {

    @Singleton
    @Provides
    fun provideAppInitialization(context: Context): AppInitialization {
        return AppInitializationImpl(context)
    }
}
