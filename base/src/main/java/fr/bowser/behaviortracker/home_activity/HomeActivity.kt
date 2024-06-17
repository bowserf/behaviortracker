package fr.bowser.behaviortracker.home_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.alarm_notification.AlarmNotificationManagerImpl
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.create_timer_view.CreateTimerViewBottomSheetFragment
import fr.bowser.behaviortracker.utils.ActivityExtension.bind
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: HomeActivityContract.Presenter

    private val screen = createScreen()

    private val bottomNavigationView: BottomNavigationView by bind(
        R.id.home_activity_bottom_navigation,
    )
    private val shadowBottomNavigationView: View by bind(
        R.id.home_activity_bottom_navigation_shadow,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.home_activity)

        setupGraph()

        setupNavigation()

        manageIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        manageIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.home_activity_nav_host_fragment).navigateUp()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onPause() {
        presenter.onStop()
        super.onPause()
    }

    private fun setupGraph() {
        val build = DaggerHomeActivityComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
            .homeActivityModule(HomeActivityModule(screen))
            .build()
        build.inject(this)
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.home_activity_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

        val navigationManager = BehaviorTrackerApp.getAppComponent(this).provideNavigationManager()
        navigationManager.setNavigationController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_activity_timer_list_screen -> showBottomNav()
                R.id.home_activity_pomodoro_screen -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE
        shadowBottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNav() {
        shadowBottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun manageIntent(intent: Intent) {
        when (intent.action) {
            ACTION_CREATE_TIMER_FROM_SHORTCUT -> CreateTimerViewBottomSheetFragment.showDialog(
                this,
                false,
            )

            ACTION_SELECT_POMODORO_TIMER -> displayPomodoroScreen(true)
            AlarmNotificationManagerImpl.ACTION_ALARM_NOTIFICATION_CLICKED -> presenter.onAlarmNotificationClicked()
        }
    }

    private fun displayPomodoroScreen(displaySelectTimer: Boolean) {
        presenter.navigateToPomodoroScreen(displaySelectTimer)
    }

    private fun createScreen() = object : HomeActivityContract.Screen {
    }

    companion object {
        const val ACTION_CREATE_TIMER_FROM_SHORTCUT = "android.intent.action.CREATE_TIMER"
        const val ACTION_SELECT_POMODORO_TIMER = "android.intent.action.select_pomodoro_timer"
    }
}
