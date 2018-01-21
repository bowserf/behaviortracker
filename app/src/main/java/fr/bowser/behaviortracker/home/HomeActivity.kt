package fr.bowser.behaviortracker.home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.stat.StatFragment
import fr.bowser.behaviortracker.timer.TimerFragment
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View {

    @Inject
    lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupGraph()

        initializeToolbar()

        initializeBottomBar()

        presenter.start()
    }

    override fun displayTimerView() {
        displayFragment(TimerFragment() , TimerFragment.TAG)
    }

    override fun displayTimelineView() {
        displayFragment(StatFragment() , StatFragment.TAG)
    }

    private fun initializeToolbar() {
        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(myToolbar)
    }

    private fun displayFragment(fragment:Fragment, tag: String){
        supportFragmentManager.inTransaction {
            replace(R.id.fragment_container, fragment, tag)
        }
    }

    private fun setupGraph(){
        val build = DaggerHomeComponent.builder()
                .homeModule(HomeModule(this))
                .build()
        build.inject(this)
    }

    private fun initializeBottomBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_navigation_timer -> presenter.onClickTimer()
                R.id.bottom_navigation_timeline -> presenter.onClickTimeline()
            }
            true
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

}
