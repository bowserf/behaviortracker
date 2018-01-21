package fr.bowser.behaviortracker.utils

object TimeConverter {

    fun convertSecondsToHumanTime(time: Long): String{
        var remainingTime = time

        val hours = remainingTime / 3600
        remainingTime %= 3600
        val minutes = remainingTime / 60
        remainingTime %= 60
        val seconds = remainingTime

        return " $hours:$minutes:$seconds"
    }

}