package fr.bowser.behaviortracker.show_mode

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.home_activity.HomeActivity
import fr.bowser.behaviortracker.screenshot.Screenshot
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_list_view.TimerListViewAdapter
import fr.bowser.behaviortracker.utils.ColorUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShowModeTest {

    @get:Rule
    var nameRule = TestName()

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun timerListIsDisplayed() {
        setupTimers()

        onView(withId(R.id.timer_list_view_list_timers))
            .perform(
                actionOnItem<TimerListViewAdapter.TimerViewHolder>(
                    hasDescendant(withText("New timer")),
                    click(),
                ).atPosition(0),
            )

        takeScreenshot("timer_list")
    }

    private fun setupTimers() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val applicationContext = instrumentation.targetContext
        val appComponent = BehaviorTrackerApp.getAppComponent(applicationContext)
        val timerRepository = appComponent.provideTimerRepositoryManager()
        instrumentation.runOnMainSync {
            timerRepository.addTimer(
                Timer(
                    "New timer",
                    ColorUtils.COLOR_BLUE,
                ),
            )
        }
    }

    private fun takeScreenshot(name: String) {
        Screenshot.takeScreenshot("${javaClass.simpleName}_${nameRule.methodName}_$name")
    }
}
