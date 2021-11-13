package fr.bowser.behaviortracker.app.instantapp

import android.app.Activity
import fr.bowser.behaviortracker.instantapp.InstantAppManager

class InstantAppManagerImpl : InstantAppManager {
    override fun showInstallPrompt(activity: Activity) {
        // nothing to do
    }

    override fun isInstantApp(): Boolean {
        return false
    }
}
