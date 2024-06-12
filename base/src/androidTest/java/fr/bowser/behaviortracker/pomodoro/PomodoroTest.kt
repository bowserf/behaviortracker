package fr.bowser.behaviortracker.pomodoro

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import fr.bowser.behaviortracker.choose_pomodoro.ChoosePomodoroRobot
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.home.HomeTabRobot
import fr.bowser.behaviortracker.home_activity.HomeActivity
import fr.bowser.behaviortracker.screenshot.Screenshot
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PomodoroTest {

    @get:Rule
    var nameRule = TestName()

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    private val homeTabRobot = HomeTabRobot()

    private val choosePomodoroRobot = ChoosePomodoroRobot()

    private val pomodoroRobot = PomodoroRobot()

    @Test
    fun startPomodoro() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val applicationContext = instrumentation.targetContext
        val appComponent = BehaviorTrackerApp.getAppComponent(applicationContext)
        val timerRepository = appComponent.provideTimerRepositoryManager()
        val pomodoroManager = appComponent.providePomodoroManager()

        instrumentation.runOnMainSync {
            timerRepository.addTimer(
                Timer(
                    "New timer",
                    ColorUtils.COLOR_BLUE,
                )
            )
        }

        homeTabRobot.goToPomodoro()

        pomodoroRobot.clickShowChooseTimer()

        choosePomodoroRobot.selectTimer()

        takeScreenshot("running_timer_pomodoro")

        assert(pomodoroManager.isStarted)
        assert(pomodoroManager.isRunning)
    }

    private fun takeScreenshot(name: String) {
        Screenshot.takeScreenshot("${javaClass.simpleName}_${nameRule.methodName}_$name")
    }
}
