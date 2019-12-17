package fr.bowser.behaviortracker.instantapp.instantapp

import android.app.Activity
import com.google.android.gms.instantapps.InstantApps
import fr.bowser.behaviortracker.instantapp.InstantAppManager

class InstantAppManagerImpl : InstantAppManager {
    override fun showInstallPrompt(activity: Activity) {
        InstantApps.showInstallPrompt(activity, null, 0, null)
    }

    override fun isInstantApp(): Boolean {
        return true
    }
}