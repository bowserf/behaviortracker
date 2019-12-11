package fr.bowser.behaviortracker.app.app_initialization

import android.content.Context
import fr.bowser.behaviortracker.app.instantapp.DaggerInstantAppComponentImpl
import fr.bowser.behaviortracker.app_initialization.AppInitializationComponent
import fr.bowser.behaviortracker.app_initialization.AppInitializationProvider
import fr.bowser.behaviortracker.config.DaggerBehaviorTrackerAppComponent
import fr.bowser.behaviortracker.instantapp.InstantAppComponent
import fr.bowser.behaviortracker.instantapp.InstantAppManagerProvider

object AppInitializationComponentProvider : AppInitializationProvider {

    override fun provideAppInitializationComponent(context: Context): AppInitializationComponent {
        return DaggerAppInitializationComponentImpl.builder()
            .context(context)
            .build()
    }
}