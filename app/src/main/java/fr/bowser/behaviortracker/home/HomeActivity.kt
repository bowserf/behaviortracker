package fr.bowser.behaviortracker.home

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.timerlist.TimerFragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initializeToolbar()

        displayTimerFragment()
    }

    private fun initializeToolbar() {
        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(myToolbar)
    }

    private fun displayTimerFragment(){
        supportFragmentManager.inTransaction {
            replace(R.id.fragment_container, TimerFragment(), TimerFragment.TAG)
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

}
