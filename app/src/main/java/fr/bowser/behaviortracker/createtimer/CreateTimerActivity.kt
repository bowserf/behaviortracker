package fr.bowser.behaviortracker.createtimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import fr.bowser.behaviortracker.R

class CreateTimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_timer)

        initToolbar()
    }

    private fun initToolbar(){
        val myToolbar = findViewById<Toolbar>(R.id.create_timer_toolbar)
        setSupportActionBar(myToolbar)
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
    }

    companion object {
        const val TAG = "CreateTimerActivity"

        fun startActivity(context: Context?) {
            val intent = Intent(context, CreateTimerActivity::class.java)
            context?.startActivity(intent)
        }
    }

}