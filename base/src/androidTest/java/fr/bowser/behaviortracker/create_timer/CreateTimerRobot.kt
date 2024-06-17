package fr.bowser.behaviortracker.create_timer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.common.TestRobot

class CreateTimerRobot : TestRobot() {

    fun setTimerName(name: String) {
        onView(ViewMatchers.withId(R.id.creation_timer_name))
            .perform(ViewActions.typeText(name))
    }

    fun clickCreateTimer() {
        clickOnView(R.id.create_timer_view_create)
    }

    fun clickStartNow() {
        clickOnView(R.id.start_after_creation)
    }
}
