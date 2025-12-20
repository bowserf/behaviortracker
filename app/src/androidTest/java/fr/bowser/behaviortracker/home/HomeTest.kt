package fr.bowser.behaviortracker.home

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.home_activity.HomeActivity
import fr.bowser.behaviortracker.screenshot.Screenshot
import fr.bowser.behaviortracker.theme.Theme
import fr.bowser.behaviortracker.timer.CleanTimerRepositoryRule
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeTest {

    @get:Rule
    var runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @get:Rule
    var nameRule = TestName()

    // More convenient way of doing
    // val activityRule = ActivityScenarioRule(HomeActivity::class.java)
    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @get:Rule
    val cleanTimerRepositoryRule = CleanTimerRepositoryRule()

    @After
    fun tearDown() {
        Theme.createActivityScenarioRule(activityRule, false)
    }

    @Test
    fun timerListIsDisplayed() {
        setupTimers()

        onView(withId(R.id.home_activity_timer_list_screen)).check(
            ViewAssertions.matches(isDisplayed()),
        )
        onView(withId(R.id.home_activity_pomodoro_screen)).check(
            ViewAssertions.matches(isDisplayed()),
        )

        takeScreenshot("timer_list")
    }

    @Test
    fun timerListIsDisplayedDarkMode() {
        setupTimers()

        Theme.createActivityScenarioRule(activityRule, true)

        takeScreenshot("timer_list_dark_mode")
    }

    private fun setupTimers() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val applicationContext = instrumentation.targetContext
        val appComponent = BehaviorTrackerApp.getAppComponent(applicationContext)
        val timerRepository = appComponent.provideTimerRepositoryManager()
        val timeManager = appComponent.provideTimeManager()
        instrumentation.runOnMainSync {
            timerRepository.addTimer(
                Timer(
                    name = "Morning meeting",
                    color = ColorUtils.COLOR_BLUE,
                    currentTime = 8000,
                    lastUpdateTimestamp = 1727366400000,
                ),
            )
            timerRepository.addTimer(
                Timer(
                    name = "Go to work by transport",
                    color = ColorUtils.COLOR_AMBER,
                    currentTime = 1700,
                    lastUpdateTimestamp = 1727366400000,
                ),
            )
            timerRepository.addTimer(
                Timer(
                    name = "Sport",
                    color = ColorUtils.COLOR_DEEP_ORANGE,
                    currentTime = 3000,
                    lastUpdateTimestamp = 1727366400000,
                ),
            )
            timerRepository.addTimer(
                Timer(
                    name = "Get ready before going to work",
                    color = ColorUtils.COLOR_BLUE_GREY,
                    currentTime = 2000,
                    lastUpdateTimestamp = 1727366400000,
                ),
            )
            timeManager.startTimer(timerRepository.getTimerList().first())
        }
    }

    private fun takeScreenshot(name: String) {
        Screenshot.takeScreenshot("${javaClass.simpleName}_${nameRule.methodName}_$name")
    }
}
