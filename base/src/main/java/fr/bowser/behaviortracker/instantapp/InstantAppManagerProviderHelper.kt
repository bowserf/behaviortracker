package fr.bowser.behaviortracker.instantapp

import android.content.Context

object InstantAppManagerProviderHelper {

    fun provideMyInstantAppComponent(context: Context): InstantAppComponent {
        if (context is InstantAppManagerProvider) {
            return context.provideMyInstantAppComponent()
        } else {
            throw IllegalStateException("context should be a InstantAppManagerProvider.")
        }
    }
}