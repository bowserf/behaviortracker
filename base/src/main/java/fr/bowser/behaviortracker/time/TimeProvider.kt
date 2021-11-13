package fr.bowser.behaviortracker.time

interface TimeProvider {

    fun getCurrentTimeMs(): Long

    fun convertTimestampToHumanReadable(timestamp: Long): String
}
