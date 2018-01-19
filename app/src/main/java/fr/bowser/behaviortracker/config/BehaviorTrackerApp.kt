package fr.bowser.behaviortracker.config

import android.app.Application
import android.os.StrictMode
import fr.bowser.behaviortracker.BuildConfig


class BehaviorTrackerApp : Application(){

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }

        super.onCreate()
    }

}