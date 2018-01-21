package fr.bowser.behaviortracker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import fr.bowser.behaviortracker.utils.TimeConverter;

@RunWith(JUnit4ClassRunner.class)
public class TimeConverterTest {

    @Test
    public void convertSeconds() {
        String time1 = TimeConverter.convertSecondsToHumanTime(12345);
        Assert.assertEquals("03:25:45", time1);
    }

    @Test
    public void convertSecondsLessThan10hours() {
        String time1 = TimeConverter.convertSecondsToHumanTime(13632);
        Assert.assertEquals("03:47:12", time1);
    }

    @Test
    public void convertSecondsLessThan10seconds() {
        String time1 = TimeConverter.convertSecondsToHumanTime(6000);
        Assert.assertEquals("01:40:00", time1);
    }

    @Test
    public void convertSecondsLessThan10minutes() {
        String time1 = TimeConverter.convertSecondsToHumanTime(3727);
        Assert.assertEquals("01:02:07", time1);
    }

    @Test
    public void convertSecondsSmallTime() {
        String time1 = TimeConverter.convertSecondsToHumanTime(127);
        Assert.assertEquals("00:02:07", time1);
    }

}
