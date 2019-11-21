package fr.bowser.behaviortracker.instantapp

import dagger.Component
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = InstantAppComponentImpl::class)
@Component(
        modules = [InstantAppManagerModule::class]
)
interface InstantAppComponentImpl : InstantAppComponent {

    override fun provideMyInstantApp(): InstantAppManager
}