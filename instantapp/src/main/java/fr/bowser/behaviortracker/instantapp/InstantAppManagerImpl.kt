package fr.bowser.behaviortracker.instantapp

import android.app.Activity
import com.google.android.gms.instantapps.InstantApps

class InstantAppManagerImpl : InstantAppManager {
    override fun showInstallPrompt(activity: Activity) {
        InstantApps.showInstallPrompt(activity, null, 0, null)
    }

    override fun isInstantApp(): Boolean {
        return true
    }
}