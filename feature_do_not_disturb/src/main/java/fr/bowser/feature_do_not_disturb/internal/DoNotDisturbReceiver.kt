package fr.bowser.feature_do_not_disturb.internal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class DoNotDisturbReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val doNotDisturbManager = DoNotDisturbGraphInternal.getDoNotDisturbManager()
        doNotDisturbManager.updateDndState()
    }
}
