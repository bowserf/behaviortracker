package fr.bowser.behaviortracker.time_provider

import fr.bowser.behaviortracker.time_zone.TimeZoneManager
import java.text.SimpleDateFormat
import java.util.Locale

class TimeProviderImpl(
    timeZoneManager: TimeZoneManager,
) : TimeProvider {

    private val timeZoneManagerListener = createTimeZoneManagerListener()

    private var date = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())

    init {
        timeZoneManager.addListener(timeZoneManagerListener)
    }

    override fun getCurrentTimeMs(): Long {
        return System.currentTimeMillis()
    }

    override fun convertTimestampToHumanReadable(timestamp: Long): String {
        return date.format(timestamp)
    }

    private fun createTimeZoneManagerListener() = object : TimeZoneManager.Listener {
        override fun onTimeZoneChanged() {
            date = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())
        }
    }
}
