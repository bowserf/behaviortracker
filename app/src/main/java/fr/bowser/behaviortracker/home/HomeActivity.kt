package fr.bowser.behaviortracker.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.alarm.AlarmNotification
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View {

    @Inject
    lateinit var presenter: HomePresenter

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

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onPause() {
        presenter.stop()
        super.onPause()
    }

    private fun setupGraph() {
        val build = DaggerHomeComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
            .homeModule(HomeModule())
            .build()
        build.inject(this)
    }

    private fun setupNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_bottom_navigation)

        val navController = Navigation.findNavController(this, R.id.home_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)
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

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    companion object {
        const val CREATE_TIMER_FROM_SHORTCUT = "android.intent.action.CREATE_TIMER"
    }
}
