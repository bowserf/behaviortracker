package fr.bowser.behaviortracker.timer

import android.content.Context
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.utils.ColorUtils
import java.util.*

class TimerDaoUA(context: Context) : TimerDAO {

    private val timers = ArrayList<Timer>()

    init {
        val color1 = ColorUtils.convertPositionToColor(10)
        timers.add(
            Timer(
                0,
                5304L,
                context.resources.getString(R.string.release_screenshot_timer_work),
                color1,
                0
            )
        )
        val color2 = ColorUtils.convertPositionToColor(7)
        timers.add(
            Timer(
                1,
                1638L,
                context.resources.getString(R.string.release_screenshot_timer_transport),
                color2,
                1
            )
        )
        val color3 = ColorUtils.convertPositionToColor(4)
        timers.add(
            Timer(
                2,
                2700L,
                context.resources.getString(R.string.release_screenshot_timer_sport),
                color3,
                2
            )
        )
        val color4 = ColorUtils.convertPositionToColor(0)
        timers.add(
            Timer(
                3,
                4000L,
                context.resources.getString(R.string.release_screenshot_timer_cooking),
                color4,
                3
            )
        )
        val color5 = ColorUtils.convertPositionToColor(18)
        timers.add(
            Timer(
                4,
                2000L,
                context.resources.getString(R.string.release_screenshot_timer_watch_series),
                color5,
                4
            )
        )
    }

    override fun getTimers(): List<Timer> {
        return timers
    }

    override fun addTimer(timer: Timer): Long {
        val position = timers.size
        timers.add(timer)
        return position.toLong()
    }

    override fun removeTimer(timer: Timer) {
        timers.remove(timer)
    }

    override fun renameTimer(id: Long, name: String) {
        timers[id.toInt()].name = name
    }

    override fun updateTimerTime(id: Long, currentTime: Long) {
        timers[id.toInt()].time = currentTime.toFloat()
    }

    override fun updateTimerPosition(id: Long, position: Int) {
        timers[id.toInt()].position = position
    }
}