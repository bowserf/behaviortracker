package fr.bowser.behaviortracker.timer_list_view

import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.common.TestRobot

class TimerListViewRobot: TestRobot() {

    fun clickShowCreateTimer() {
        clickOnView(R.id.timer_list_view_add_timer)
    }
}
