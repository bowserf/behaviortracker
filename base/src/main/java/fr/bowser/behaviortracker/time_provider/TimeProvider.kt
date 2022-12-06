package fr.bowser.behaviortracker.time_provider

interface TimeProvider {

    fun getCurrentTimeMs(): Long

    fun convertTimestampToHumanReadable(timestamp: Long): String
}
