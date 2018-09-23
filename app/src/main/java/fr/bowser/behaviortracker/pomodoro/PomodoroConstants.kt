package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.BuildConfig

object PomodoroConstants {

    val POMODORO_DURATION = if (BuildConfig.DEBUG) 10L else (25 * 60).toLong()
    val REST_DURATION = if (BuildConfig.DEBUG) 5L else (5 * 60).toLong()

}