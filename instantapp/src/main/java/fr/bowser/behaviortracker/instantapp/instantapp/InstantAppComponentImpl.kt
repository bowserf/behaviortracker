package fr.bowser.behaviortracker.instantapp.instantapp

import dagger.Component
import fr.bowser.behaviortracker.instantapp.InstantAppComponent
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = InstantAppComponentImpl::class)
@Component(
        modules = [InstantAppManagerModule::class]
)
interface InstantAppComponentImpl : InstantAppComponent {

    override fun provideMyInstantApp(): InstantAppManager
}