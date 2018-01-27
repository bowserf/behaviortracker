package fr.bowser.behaviortracker.createtimer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.Spinner
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject

class CreateTimerActivity : AppCompatActivity(), CreateTimerContract.View {

    @Inject
    lateinit var presenter: CreateTimerPresenter

    private lateinit var editTimerName: EditText
    private lateinit var chooseColor: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_timer)

        setupGraph()

        initToolbar()

        initSpinner()

        initUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun exitViewAfterSucceedTimerCreation() {

    }

    private fun initSpinner() {
        chooseColor = findViewById(R.id.choose_color)
        val colorAdapter = ColorAdapter(this, android.R.layout.simple_spinner_item, provideColors())
        chooseColor.adapter = colorAdapter
    }

    private fun provideColors(): MutableList<Int>? {
        val colors = ArrayList<Int>()
        colors.add(Color.RED)
        colors.add(Color.BLUE)
        colors.add(Color.YELLOW)
        colors.add(Color.GREEN)
        colors.add(Color.CYAN)
        return colors
    }

    private fun setupGraph(){
        val component = DaggerCreateTimerComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
                .createTimerModule(CreateTimerModule(this))
                .build()
        component.inject(this)
    }

    private fun initToolbar(){
        val myToolbar = findViewById<Toolbar>(R.id.create_timer_toolbar)
        setSupportActionBar(myToolbar)
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
    }

    private fun initUI() {
        editTimerName = findViewById(R.id.creation_timer_name)
        findViewById<FloatingActionButton>(R.id.button_create_timer).setOnClickListener {
            val timerName = editTimerName.text.toString()
            val color: Int = chooseColor.adapter.getItem(chooseColor.selectedItemPosition) as Int
            presenter.createTimer(timerName, color)
        }
    }

    companion object {
        const val TAG = "CreateTimerActivity"

        fun startActivity(context: Context?) {
            val intent = Intent(context, CreateTimerActivity::class.java)
            context?.startActivity(intent)
        }
    }

}