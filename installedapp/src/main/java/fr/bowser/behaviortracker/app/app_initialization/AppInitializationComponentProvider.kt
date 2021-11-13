package fr.bowser.behaviortracker.app.app_initialization

import android.content.Context
import fr.bowser.behaviortracker.app_initialization.AppInitializationComponent
import fr.bowser.behaviortracker.app_initialization.AppInitializationProvider

object AppInitializationComponentProvider : AppInitializationProvider {

    override fun provideAppInitializationComponent(context: Context): AppInitializationComponent {
        return DaggerAppInitializationComponentImpl.builder()
            .context(context)
            .build()
    }
}
