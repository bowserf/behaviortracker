package fr.bowser.behaviortracker.app.app_initialization

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.app_initialization.AppInitialization
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class AppInitializationModule {

    @GenericScope(component = AppInitializationComponentImpl::class)
    @Provides
    fun provideAppInitialization(context: Context): AppInitialization {
        return AppInitializationImpl(context)
    }
}
