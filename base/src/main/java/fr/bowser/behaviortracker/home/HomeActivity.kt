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
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: HomeContract.Presenter

    private val screen = createScreen()

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var shadowBottomNavigationView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupGraph()

        setupNavigation()

        manageIntent()

        activityStartedFromAlarmNotif()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        activityStartedFromAlarmNotif()
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.home_nav_host_fragment).navigateUp()
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
        val build = DaggerHomeComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
            .homeModule(HomeModule(screen))
            .build()
        build.inject(this)
    }

    private fun setupNavigation() {
        bottomNavigationView = findViewById(R.id.home_bottom_navigation)
        shadowBottomNavigationView = findViewById(R.id.home_bottom_navigation_shadow)

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
        shadowBottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNav() {
        shadowBottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun manageIntent() {
        if (intent.action == CREATE_TIMER_FROM_SHORTCUT) {
            CreateTimerDialog.showDialog(this, false)
        }
    }

    private fun activityStartedFromAlarmNotif() {
        if (intent.extras == null) {
            return
        }
        val isAlarmNotifClicked =
            intent.extras!!.getBoolean(AlarmNotification.INTENT_EXTRA_ALARM_REQUEST_CODE)
        if (isAlarmNotifClicked) {
            presenter.onAlarmNotificationClicked()
        }
    }

    private fun createScreen() = object : HomeContract.Screen {
        override fun setupInstantAppButton() {
            val installBtn = findViewById<View>(R.id.home_instant_app_install)
            installBtn.visibility = View.VISIBLE
            installBtn.setOnClickListener {
                presenter.onClickInstallApp()
            }
        }
    }

    companion object {
        const val CREATE_TIMER_FROM_SHORTCUT = "android.intent.action.CREATE_TIMER"
    }
}
