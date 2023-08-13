package fr.bowser.behaviortracker.utils

object TimeConverter {

    private const val SECONDS_IN_AN_HOUR = 3600
    private const val MINUTES_IN_AN_HOUR = 60
    private const val LIMIT_BETWEEN_ONE_AND_TWO_DIGIT = 10

    @JvmStatic
    fun convertSecondsToHumanTime(
        time: Long,
        mode: DisplayHoursMode = DisplayHoursMode.Always
    ): String {
        if (time < 0) {
            throw IllegalStateException("time can't be negative")
        }

        var remainingTime = time
        var string = ""

        val hours = remainingTime / SECONDS_IN_AN_HOUR

        if (mode == DisplayHoursMode.Always || (mode == DisplayHoursMode.IfPossible && hours > 0)) {
            if (hours < LIMIT_BETWEEN_ONE_AND_TWO_DIGIT) {
                string = "0"
            }
            string = "$string$hours:"

            remainingTime %= SECONDS_IN_AN_HOUR
        }

        val minutes = remainingTime / MINUTES_IN_AN_HOUR
        if (minutes < LIMIT_BETWEEN_ONE_AND_TWO_DIGIT) {
            string += "0"
        }
        string = "$string$minutes:"

        remainingTime %= MINUTES_IN_AN_HOUR

        val seconds = remainingTime
        if (seconds < LIMIT_BETWEEN_ONE_AND_TWO_DIGIT) {
            string += "0"
        }
        string += seconds

        return string
    }

    enum class DisplayHoursMode {
        Always,
        IfPossible,
        Never
    }
}
