package fr.bowser.behaviortracker.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import fr.bowser.behaviortracker.R

class
SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initializeToolbar()

        initSettingFragment()
    }

    private fun initializeToolbar() {
        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        myToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initSettingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingFragment())
            .commit()
    }

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}