package fr.bowser.behaviortracker.notification

import fr.bowser.behaviortracker.timer.Timer

interface TimerNotificationManager {

    var timer: Timer?

    fun changeOngoingState(isAppInBackground: Boolean)

    fun dismissNotification(killProcess: Boolean = true)

}