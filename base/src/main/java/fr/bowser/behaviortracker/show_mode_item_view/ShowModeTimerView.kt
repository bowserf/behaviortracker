package fr.bowser.behaviortracker.show_mode_item_view

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.RippleDrawable
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject

class ShowModeTimerView(context: Context) : LinearLayout(context) {

    @Inject
    lateinit var presenter: ShowModeTimerViewContract.Presenter

    private val screen = createScreen()

    private val chrono: TextView
    private val timerName: TextView
    private val timerState: ImageView

    init {
        setupGraph()

        inflate(context, R.layout.item_show_mode, this)

        orientation = VERTICAL
        gravity = Gravity.CENTER

        setOnClickListener { presenter.onClickView() }

        setBackgroundResource(R.drawable.bg_show_mode_item)

        chrono = findViewById(R.id.show_mode_timer_time)
        timerName = findViewById(R.id.show_mode_timer_name)
        timerState = findViewById(R.id.show_mode_timer_status)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onStart()
    }

    override fun onDetachedFromWindow() {
        presenter.onStop()
        super.onDetachedFromWindow()
    }

    fun setTimer(timer: Timer) {
        presenter.setTimer(timer)

        val drawable = background as RippleDrawable
        drawable.setColorFilter(ColorUtils.getColor(context, timer.color), PorterDuff.Mode.SRC_ATOP)

        chrono.text = TimeConverter.convertSecondsToHumanTime(timer.time.toLong())
        timerName.text = timer.name
    }

    private fun createScreen() = object : ShowModeTimerViewContract.Screen {
        override fun statusUpdated(activate: Boolean) {
            if (activate) {
                timerState.setImageResource(R.drawable.ic_pause)
            } else {
                timerState.setImageResource(R.drawable.ic_play)
            }
        }

        override fun timerUpdated(newTime: Long) {
            chrono.text = TimeConverter.convertSecondsToHumanTime(newTime)
        }
    }

    private fun setupGraph() {
        val component = DaggerShowModeTimerViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .showModeTimerViewModule(ShowModeTimerViewModule(screen))
            .build()
        component.inject(this)
    }
}
