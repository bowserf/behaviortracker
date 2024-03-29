package fr.bowser.behaviortracker.utils

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`

class TimeConverterTest {

    @Test
    fun convertSeconds() {
        val time1 = TimeConverter.convertSecondsToHumanTime(12345)
        Assert.assertEquals("03:25:45", time1)
    }

    @Test
    fun convertSecondsLessThan10hours() {
        val time1 = TimeConverter.convertSecondsToHumanTime(13632)
        Assert.assertEquals("03:47:12", time1)
    }

    @Test
    fun convertSecondsLessThan10seconds() {
        val time1 = TimeConverter.convertSecondsToHumanTime(6000)
        Assert.assertEquals("01:40:00", time1)
    }

    @Test
    fun convertSecondsLessThan10minutes() {
        val time1 = TimeConverter.convertSecondsToHumanTime(3727)
        Assert.assertEquals("01:02:07", time1)
    }

    @Test
    fun convertSecondsSmallTime() {
        val time1 = TimeConverter.convertSecondsToHumanTime(127)
        Assert.assertEquals("00:02:07", time1)
    }

    @Test
    fun convertSecondsToMinutes() {
        val time1 =
            TimeConverter.convertSecondsToHumanTime(127, TimeConverter.DisplayHoursMode.Never)
        Assert.assertEquals("02:07", time1)
    }

    @Test
    fun convertSecondsToHour() {
        val time1 =
            TimeConverter.convertSecondsToHumanTime(3601, TimeConverter.DisplayHoursMode.Never)
        Assert.assertEquals("60:01", time1)
    }

    @Test(expected = IllegalStateException::class)
    fun cantAcceptNegativeTime() {
        `when`(TimeConverter.convertSecondsToHumanTime(-1)).thenThrow(IllegalStateException())
    }
}
