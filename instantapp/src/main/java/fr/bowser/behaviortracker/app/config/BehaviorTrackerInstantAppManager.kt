package fr.bowser.behaviortracker.app.config

import fr.bowser.behaviortracker.app.instantapp.DaggerInstantAppComponentImpl
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.instantapp.InstantAppComponent
import fr.bowser.behaviortracker.instantapp.InstantAppManagerProvider

class BehaviorTrackerInstantAppManager : BehaviorTrackerApp(), InstantAppManagerProvider {
    override fun provideMyInstantAppComponent(): InstantAppComponent {
        return DaggerInstantAppComponentImpl.builder().build()
    }
}