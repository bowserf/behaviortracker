package fr.bowser.behaviortracker.time

class TimeProviderImpl : TimeProvider {

    override fun getCurrentTimeMs(): Long {
        return System.currentTimeMillis()
    }
}