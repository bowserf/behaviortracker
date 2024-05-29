package fr.bowser.behaviortracker.navigation

import android.os.Bundle
import androidx.navigation.NavController
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.pomodoro_view.PomodoroViewFragment

class NavigationManagerImpl : NavigationManager {

    private var navController: NavController? = null

    override fun setNavigationController(navController: NavController) {
        this.navController = navController
    }

    override fun navigateToPomodoro(displaySelectTimer: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(PomodoroViewFragment.EXTRA_KEY_DISPLAY_SELECT_TIMER, displaySelectTimer)
        navController?.navigate(R.id.home_activity_pomodoro_screen, bundle)
    }
}
