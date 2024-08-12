package fr.bowser.behaviortracker.time_zone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fr.bowser.behaviortracker.config.BehaviorTrackerApp

class TimeZoneReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        BehaviorTrackerApp.getAppComponent(context).provideTimezoneManager().notifyTimeZoneUpdated()
    }
}
