package fr.bowser.behaviortracker.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils.replace
import android.view.Menu
import android.view.MenuItem
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.alarm.AlarmNotification
import fr.bowser.behaviortracker.alarm.AlarmTimerDialog
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import fr.bowser.behaviortracker.pomodoro.PomodoroFragment
import fr.bowser.behaviortracker.setting.SettingActivity
import fr.bowser.behaviortracker.timerlist.TimerFragment
import javax.inject.Inject


class HomeActivity : AppCompatActivity(), HomeContract.View {

    @Inject
    lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupGraph()

        initializeToolbar()

        initializeBottomNavigationView()

        presenter.initialize()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_reset_all -> {
                presenter.onClickResetAll()
                return true
            }
            R.id.menu_settings -> {
                presenter.onClickSettings()
                return true
            }
            R.id.menu_alarm -> {
                presenter.onClickAlarm()
            }
        }
        return false
    }

    override fun displayResetAllDialog() {
        val message = resources.getString(R.string.home_dialog_confirm_reset_all_timers)
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    presenter.onClickResetAllTimers()
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    // do nothing
                }
                .show()
    }

    override fun displaySettingsView() {
        SettingActivity.startActivity(this)
    }

    override fun displayAlarmTimerDialog() {
        val fragmentManager = supportFragmentManager
        val alertDialog = AlarmTimerDialog.newInstance()
        alertDialog.show(fragmentManager, AlarmTimerDialog.TAG)
    }

    override fun displayPomodoroView() {
        supportFragmentManager.inTransaction {
            replace(R.id.fragment_container, PomodoroFragment(), PomodoroFragment.TAG)
        }
    }

    override fun displayTimerView() {
        supportFragmentManager.inTransaction {
            replace(R.id.fragment_container, TimerFragment(), TimerFragment.TAG)
        }
    }

    private fun setupGraph() {
        val build = DaggerHomeComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
                .homeModule(HomeModule(this))
                .build()
        build.inject(this)
    }

    private fun initializeToolbar() {
        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(myToolbar)
    }

    private fun initializeBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_timer_view -> { presenter.onClickTimerView() }
                R.id.menu_pomodoro_view -> { presenter.onClickPomodoroView() }
            }
            true
        }
    }

    private fun manageIntent() {
        if (intent.action == CREATE_TIMER_FROM_SHORTCUT) {
            CreateTimerDialog.showDialog(this, true)
        }
    }

    private fun activityStartedFromAlarmNotif(){
        if(intent.extras == null) {
            return
        }
        val isAlarmNotifClicked = intent.extras.getBoolean(AlarmNotification.INTENT_EXTRA_ALARM_REQUEST_CODE)
        if(isAlarmNotifClicked){
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
