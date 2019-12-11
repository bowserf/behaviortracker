package fr.bowser.behaviortracker.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.alarm.AlarmNotification
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import fr.bowser.behaviortracker.pomodoro.PomodoroFragment
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.Screen {

    @Inject
    lateinit var presenter: HomePresenter

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var shadowBbottomNavigationView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupGraph()

        setupNavigation()

        manageIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        manageIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.home_nav_host_fragment).navigateUp()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onPause() {
        presenter.stop()
        super.onPause()
    }

    override fun setupInstantAppButton() {
        val installBtn = findViewById<View>(R.id.home_instant_app_install)
        installBtn.visibility = View.VISIBLE
        installBtn.setOnClickListener {
            presenter.onClickInstallApp()
        }
    }

    private fun setupGraph() {
        val build = DaggerHomeComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
            .homeModule(HomeModule(this))
            .build()
        build.inject(this)
    }

    private fun setupNavigation() {
        bottomNavigationView = findViewById(R.id.home_bottom_navigation)
        shadowBbottomNavigationView = findViewById(R.id.home_bottom_navigation_shadow)

        val navController = Navigation.findNavController(this, R.id.home_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.timer_list_screen -> showBottomNav()
                R.id.pomodoro_screen -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE
        shadowBbottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNav() {
        shadowBbottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun manageIntent(intent: Intent) {
        when (intent.action) {
            ACTION_CREATE_TIMER_FROM_SHORTCUT -> CreateTimerDialog.showDialog(this, false)
            ACTION_SELECT_POMODORO_TIMER -> displayPomodoroScreen(true)
            AlarmNotification.ACTION_ALARM_NOTIFICATION_CLICKED -> presenter.onAlarmNotificationClicked()
        }
    }

    private fun displayPomodoroScreen(displaySelectTimer: Boolean) {
        val navController = Navigation.findNavController(this, R.id.home_nav_host_fragment)
        val bundle = Bundle()
        bundle.putBoolean(PomodoroFragment.EXTRA_KEY_DISPLAY_SELECT_TIMER, displaySelectTimer)
        navController.navigate(R.id.pomodoro_screen, bundle)
    }

    companion object {
        const val ACTION_CREATE_TIMER_FROM_SHORTCUT = "android.intent.action.CREATE_TIMER"
        const val ACTION_SELECT_POMODORO_TIMER = "android.intent.action.select_pomodoro_timer"
    }
}
