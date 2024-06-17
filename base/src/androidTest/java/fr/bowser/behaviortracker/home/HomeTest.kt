package fr.bowser.behaviortracker.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.home_activity.HomeActivity
import fr.bowser.behaviortracker.screenshot.Screenshot
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeTest {

    @get:Rule
    var nameRule = TestName()

    // More convenient way of doing
    // val activityRule = ActivityScenarioRule(HomeActivity::class.java)
    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun timerListIsDisplayed() {
        setupTimers()

        onView(withId(R.id.home_activity_timer_list_screen)).check(
            ViewAssertions.matches(
                isDisplayed()
            )
        )
        onView(withId(R.id.home_activity_pomodoro_screen)).check(
            ViewAssertions.matches(isDisplayed())
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
                )
            )
            timerRepository.addTimer(
                Timer(
                    "New timer",
                    ColorUtils.COLOR_AMBER,
                )
            )
            timerRepository.addTimer(
                Timer(
                    "New timer",
                    ColorUtils.COLOR_DEEP_ORANGE,
                )
            )
            timerRepository.addTimer(
                Timer(
                    "New timer",
                    ColorUtils.COLOR_LIGHT_BLUE,
                )
            )
            timerRepository.addTimer(
                Timer(
                    "New timer",
                    ColorUtils.COLOR_PURPLE,
                )
            )
            timerRepository.addTimer(
                Timer(
                    "New timer",
                    ColorUtils.COLOR_YELLOW,
                )
            )
        }
    }

    private fun takeScreenshot(name: String) {
        Screenshot.takeScreenshot("${javaClass.simpleName}_${nameRule.methodName}_$name")
    }
}
