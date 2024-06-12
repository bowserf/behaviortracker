package fr.bowser.behaviortracker.choose_pomodoro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.common.TestRobot
import fr.bowser.behaviortracker.pomodoro_choose_timer_view.PomodoroChooseTimerViewAdapter

class ChoosePomodoroRobot : TestRobot() {

    fun selectTimer() {
        onView(ViewMatchers.withId(R.id.choose_pomodoro_timer_view_timers))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<PomodoroChooseTimerViewAdapter.ChooseTimerHolder>(
                    0,
                    click()
                )
            )
    }
}
