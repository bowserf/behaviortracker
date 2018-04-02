package fr.bowser.behaviortracker.utils

object TimeConverter {

    @JvmStatic
    fun convertSecondsToHumanTime(time: Long): String {
        var remainingTime = time
        var string = ""

        val hours = remainingTime / 3600

        if (hours < 10) {
            string = "0"
        }
        string = "$string$hours:"

        remainingTime %= 3600

        val minutes = remainingTime / 60
        if (minutes < 10) {
            string += "0"
        }
        string = "$string$minutes:"

        remainingTime %= 60

        val seconds = remainingTime
        if (seconds < 10) {
            string += "0"
        }
        string += seconds

        return string
    }

}