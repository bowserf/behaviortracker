package fr.bowser.behaviortracker.notification_manager

interface NotificationManager {

    fun areNotificationsEnabled(): Boolean

    fun displayNotificationSettings()
}
