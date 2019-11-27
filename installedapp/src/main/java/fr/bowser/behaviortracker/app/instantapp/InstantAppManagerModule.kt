package fr.bowser.behaviortracker.app.instantapp

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class InstantAppManagerModule {

    @GenericScope(component = InstantAppComponentImpl::class)
    @Provides
    fun provideMyInstantApp(): InstantAppManager {
        return InstantAppManagerImpl()
    }
}