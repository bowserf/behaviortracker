package fr.bowser.behaviortracker.showmode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import javax.inject.Inject

class ShowModeActivity : AppCompatActivity(), ShowModeContract.View {

    private lateinit var adapter: ShowModeAdapter
    private lateinit var viewPager: ViewPager

    @Inject
    lateinit var presenter: ShowModePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_mode)

        setupGraph()

        initUI()

        val extras = intent.extras
        val selectedTimerID = extras.getLong(EXTRA_SELECTED_TIMER_ID)
        presenter.start(selectedTimerID)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupGraph() {
        val build = DaggerShowModeComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
                .showModeModule(ShowModeModule(this))
                .build()
        build.inject(this)
    }

    private fun initUI() {
        viewPager = findViewById(R.id.show_mode_pager)
        adapter = ShowModeAdapter()
        viewPager.adapter = adapter

        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.show_mode_toolbar)
        setSupportActionBar(toolbar)
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.title = ""
    }

    override fun displayTimerList(timers: List<Timer>, selectedTimerPosition: Int) {
        adapter.setData(timers)
        viewPager.currentItem = selectedTimerPosition
    }

    companion object {

        private const val EXTRA_SELECTED_TIMER_ID = "show_mode_activity.key.extra_selected_timer_id"

        fun startActivity(context: Context, selectedTimerId: Long) {
            val intent = Intent(context, ShowModeActivity::class.java)
            intent.putExtra(EXTRA_SELECTED_TIMER_ID, selectedTimerId)
            context.startActivity(intent)
        }

    }

}