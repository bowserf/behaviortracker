package fr.bowser.behaviortracker.instantapp.app_initialization

import android.content.Context
import fr.bowser.behaviortracker.app_initialization.AppInitializationComponent
import fr.bowser.behaviortracker.app_initialization.AppInitializationProvider

object AppInitializationComponentProvider : AppInitializationProvider {

    override fun provideAppInitializationComponent(context: Context): AppInitializationComponent {
        return DaggerAppInitializationComponentImpl.create()
    }
}