package fr.bowser.behaviortracker.instantapp.app_initialization

import dagger.Component
import fr.bowser.behaviortracker.app_initialization.AppInitialization
import fr.bowser.behaviortracker.app_initialization.AppInitializationComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = AppInitializationComponentImpl::class)
@Component(
    modules = [AppInitializationModule::class]
)
interface AppInitializationComponentImpl : AppInitializationComponent {

    override fun provideAppInitialization(): AppInitialization
}
