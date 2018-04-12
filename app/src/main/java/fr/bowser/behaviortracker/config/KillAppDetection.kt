package fr.bowser.behaviortracker.config

import android.app.Service
import android.content.Intent
import android.os.IBinder
import fr.bowser.behaviortracker.notification.TimerNotificationManager

class KillAppDetection : Service() {

    private lateinit var notificationManager : TimerNotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = BehaviorTrackerApp.getAppComponent(applicationContext).provideTimerNotificationManager()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        notificationManager.removeNotification(false)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}