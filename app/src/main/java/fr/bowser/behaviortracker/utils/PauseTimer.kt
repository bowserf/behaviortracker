package fr.bowser.behaviortracker.utils

import android.content.Context
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.timer.Timer

object PauseTimer {

    fun getTimer(context: Context): Timer {
        return Timer(context.getString(R.string.pomodoro_timer_pause), ColorUtils.COLOR_BLUE_GREY)
    }

}