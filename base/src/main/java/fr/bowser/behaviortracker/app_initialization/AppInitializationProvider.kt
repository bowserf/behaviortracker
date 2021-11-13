package fr.bowser.behaviortracker.app_initialization

import android.content.Context

interface AppInitializationProvider {

    fun provideAppInitializationComponent(context: Context): AppInitializationComponent
}
