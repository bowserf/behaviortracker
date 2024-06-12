package fr.bowser.behaviortracker.common

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId

open class TestRobot {

    fun clickOnView(res: Int) {
        onView(withId(res)).perform(ViewActions.click())
    }
}
