package fr.bowser.behaviortracker.showmodeitem

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.RippleDrawable
import android.support.constraint.ConstraintLayout
import android.widget.ImageView
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject

class ShowModeTimerView(context: Context) : ConstraintLayout(context),
        ShowModeTimerViewContract.View {

    @Inject
    lateinit var presenter: ShowModeTimerViewPresenter

    private val chrono: TextView
    private val timerName: TextView
    private val timerState: ImageView

    init {
        setupGraph()

        inflate(context, R.layout.item_show_mode, this)

        setOnClickListener { presenter.onClickView() }

        setBackgroundResource(R.drawable.bg_show_mode_item)

        chrono = findViewById(R.id.show_mode_timer_time)
        timerName = findViewById(R.id.show_mode_timer_name)
        timerState = findViewById(R.id.show_mode_timer_status)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.start()
    }

    override fun onDetachedFromWindow() {
        presenter.stop()
        super.onDetachedFromWindow()
    }

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

    fun setTimer(timer: Timer) {
        presenter.setTimer(timer)

        val drawable = background as RippleDrawable
        drawable.setColorFilter(ColorUtils.getColor(context, timer.color), PorterDuff.Mode.SRC_ATOP)

        chrono.text = TimeConverter.convertSecondsToHumanTime(timer.currentTime)
        timerName.text = timer.name
    }

    private fun setupGraph() {
        val component = DaggerShowModeTimerViewComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
                .showModeTimerViewModule(ShowModeTimerViewModule(this))
                .build()
        component.inject(this)
    }

}