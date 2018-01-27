package fr.bowser.behaviortracker.timeritem

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.PopupMenu
import android.widget.ImageView
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject


class TimerRowView(context: Context) : ConstraintLayout(context), TimerItemContract.View {

    private val chrono: TextView
    private val name: TextView
    private val menu: ImageView
    private val reduceChrono: TextView
    private val increaseChrono: TextView

    @Inject
    lateinit var presenter: TimerItemPresenter

    init {
        setupGraph()

        inflate(context, R.layout.item_timer, this)

        val padding = resources.getDimensionPixelOffset(R.dimen.default_space)
        setPadding(padding, padding, padding, padding)

        setOnClickListener { presenter.onTimerStateChange() }

        chrono = findViewById(R.id.timer_chrono)
        name = findViewById(R.id.timer_name)
        menu = findViewById(R.id.timer_menu)
        menu.setOnClickListener{ displayMenu() }
        reduceChrono = findViewById(R.id.timer_reduce_time)
        reduceChrono.setOnClickListener { presenter.onClickDecreaseTime() }
        increaseChrono = findViewById(R.id.timer_increase_time)
        increaseChrono.setOnClickListener { presenter.onClickIncreaseTime() }
    }

    fun setTimer(timer: Timer) {
        chrono.text = TimeConverter.convertSecondsToHumanTime(timer.currentTime)
        name.text = timer.name
        setBackgroundColor(timer.color)
    }

    override fun timerUpdated(newTime: Long) {
        chrono.text = TimeConverter.convertSecondsToHumanTime(newTime)
    }

    private fun displayMenu() {
        val popup = PopupMenu(context, menu)

        popup.menuInflater.inflate(R.menu.menu_item_timer, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.item_timer_reset -> {}
                R.id.item_timer_delete -> {}
                R.id.item_timer_rename -> {}
            }
            true
        }

        popup.show()
    }

    private fun setupGraph() {
        val component = DaggerTimerItemComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
                .timerItemModule(TimerItemModule(this))
                .build()
        component.inject(this)
    }

}