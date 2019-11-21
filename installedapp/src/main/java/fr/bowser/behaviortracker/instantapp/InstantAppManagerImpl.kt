package fr.bowser.behaviortracker.instantapp

import android.app.Activity

class InstantAppManagerImpl : InstantAppManager {
    override fun showInstallPrompt(activity: Activity) {
        // nothing to do
    }

    override fun isInstantApp(): Boolean {
        return false
    }
}