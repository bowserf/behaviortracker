package fr.bowser.behaviortracker.timer

import android.content.Context
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.utils.ColorUtils
import java.util.*

class TimerDaoUA(context: Context) : TimerDAO {

    private val timers = ArrayList<Timer>()

    init {
        timers.add(Timer(0, 5304L, context.resources.getString(R.string.release_screenshot_timer_work), ColorUtils.convertPositionToColor(10), 0))
        timers.add(Timer(1, 1638L, context.resources.getString(R.string.release_screenshot_timer_transport), ColorUtils.convertPositionToColor(7), 1))
        timers.add(Timer(2, 2700L, context.resources.getString(R.string.release_screenshot_timer_sport), ColorUtils.convertPositionToColor(4), 2))
        timers.add(Timer(3, 4000L, context.resources.getString(R.string.release_screenshot_timer_cooking), ColorUtils.convertPositionToColor(0), 3))
        timers.add(Timer(4, 2000L, context.resources.getString(R.string.release_screenshot_timer_watch_series), ColorUtils.convertPositionToColor(18), 4))
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