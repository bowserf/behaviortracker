package fr.bowser.behaviortracker.config

import fr.bowser.behaviortracker.instantapp.DaggerInstantAppComponentImpl
import fr.bowser.behaviortracker.instantapp.InstantAppComponent
import fr.bowser.behaviortracker.instantapp.InstantAppManagerProvider

class BehaviorTrackerInstantAppManager : BehaviorTrackerApp(), InstantAppManagerProvider {
    override fun provideMyInstantAppComponent(): InstantAppComponent {
        return DaggerInstantAppComponentImpl.builder().build()
    }
}