package fr.bowser.behaviortracker.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.test.ext.junit.rules.ActivityScenarioRule
import fr.bowser.behaviortracker.home_activity.HomeActivity

object Theme {

    fun createActivityScenarioRule(
        activityRule: ActivityScenarioRule<HomeActivity>,
        withNightMode: Boolean = false,
    ) =
        activityRule.scenario.apply {
            onActivity {
                AppCompatDelegate.setDefaultNightMode(
                    if (withNightMode) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    },
                )
            }
        }
}
