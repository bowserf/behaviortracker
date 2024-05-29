package fr.bowser.behaviortracker.navigation

import androidx.navigation.NavController

interface NavigationManager {

    fun setNavigationController(navController: NavController)

    fun navigateToPomodoro(displaySelectTimer: Boolean)
}
