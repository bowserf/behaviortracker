package fr.bowser.behaviortracker.create_timer

import android.Manifest
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.common.RecyclerViewMatcher.atPosition
import fr.bowser.behaviortracker.home_activity.HomeActivity
import fr.bowser.behaviortracker.screenshot.Screenshot
import fr.bowser.behaviortracker.timer_list_view.TimerListViewAdapter
import fr.bowser.behaviortracker.timer_list_view.TimerListViewRobot
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateTimerTest {

    @get:Rule
    var runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @get:Rule
    var nameRule = TestName()

    // More convenient way of doing
    // val activityRule = ActivityScenarioRule(HomeActivity::class.java)
    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    private val createTimerRobot = CreateTimerRobot()

    private val timerListViewRobot = TimerListViewRobot()

    @Test
    fun createTimer() {
        timerListViewRobot.clickShowCreateTimer()

        createTimerRobot.setTimerName("New timer")

        closeSoftKeyboard()

        takeScreenshot("set_timer_name")

        createTimerRobot.clickStartNow()

        createTimerRobot.clickCreateTimer()

        onView(withId(R.id.timer_list_view_list_timers))
            .perform(scrollToPosition<TimerListViewAdapter.TimerViewHolder>(0))
            .check(matches(atPosition(0, hasDescendant(withText("New timer")))))
    }

    private fun takeScreenshot(name: String) {
        Screenshot.takeScreenshot("${javaClass.simpleName}_${nameRule.methodName}_$name")
    }
}
