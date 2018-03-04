package fr.bowser.behaviortracker.home

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
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

        displayTimerFragment()
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
        when(item?.itemId){
            R.id.menu_reset_all ->  {
                presenter.onClickResetAll()
                return true
            }
        }
        return false
    }

    override fun displayResetAllDialog() {
        val message = resources.getString(R.string.home_dialog_confirm_reset_all_timers)
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
                .setPositiveButton(android.R.string.yes, { dialog, which ->
                    presenter.onClickResetAllTimers()
                })
                .setNegativeButton(android.R.string.no, { dialog, which ->
                    // do nothing
                })
                .show()
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

    private fun displayTimerFragment() {
        supportFragmentManager.inTransaction {
            replace(R.id.fragment_container, TimerFragment(), TimerFragment.TAG)
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

}
