package fr.bowser.behaviortracker.instantapp

import android.app.Activity

interface InstantAppManager {

    fun showInstallPrompt(activity: Activity)

    fun isInstantApp(): Boolean
}