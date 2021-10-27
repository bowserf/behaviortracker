package fr.bowser.behaviortracker.time

import java.text.SimpleDateFormat
import java.util.Locale

class TimeProviderImpl : TimeProvider {

    private val date = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())

    override fun getCurrentTimeMs(): Long {
        return System.currentTimeMillis()
    }

    override fun convertTimestampToHumanReadable(timestamp: Long): String {
        return date.format(timestamp)
    }
}