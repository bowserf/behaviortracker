package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.common.TestRobot

class PomodoroRobot: TestRobot() {

    fun clickShowChooseTimer() {
        clickOnView(R.id.pomodoro_view_choose_timer)
    }
}
