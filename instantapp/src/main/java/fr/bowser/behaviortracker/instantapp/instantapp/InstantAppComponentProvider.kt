package fr.bowser.behaviortracker.instantapp.instantapp

import fr.bowser.behaviortracker.instantapp.InstantAppComponent
import fr.bowser.behaviortracker.instantapp.InstantAppManagerProvider

object InstantAppComponentProvider : InstantAppManagerProvider {
    override fun provideMyInstantAppComponent(): InstantAppComponent {
        return DaggerInstantAppComponentImpl.builder().build()
    }
}