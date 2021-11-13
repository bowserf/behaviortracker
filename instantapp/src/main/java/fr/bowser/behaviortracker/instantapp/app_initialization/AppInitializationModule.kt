package fr.bowser.behaviortracker.instantapp.app_initialization

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.app_initialization.AppInitialization
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class AppInitializationModule {

    @GenericScope(component = AppInitializationComponentImpl::class)
    @Provides
    fun provideAppInitialization(): AppInitialization {
        return AppInitializationImpl()
    }
}
