package fr.bowser.behaviortracker

import fr.bowser.behaviortracker.utils.TimeConverter
import org.junit.Assert
import org.junit.Test

class TimerConverterTest {

    @Test
    fun convertSecondsToHumanTimeTest() {
        val time1 = TimeConverter.convertSecondsToHumanTime(13632)
        Assert.assertEquals("3:47:12", time1)
    }

}
