package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.common.TestRobot

class HomeTabRobot : TestRobot() {

    fun goToPomodoro() {
        clickOnView(R.id.home_activity_pomodoro_screen)
    }
}
