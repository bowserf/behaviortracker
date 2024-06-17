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
import fr.bowser.behaviortracker.utils.ViewExtension.bind
import javax.inject.Inject

class ShowModeItemView(context: Context) : LinearLayout(context) {

    @Inject
    lateinit var presenter: ShowModeItemViewContract.Presenter

    private val screen = createScreen()

    private val chrono: TextView by bind(R.id.show_mode_view_timer_time)
    private val timerName: TextView by bind(R.id.show_mode_view_timer_name)
    private val timerState: ImageView by bind(R.id.show_mode_view_timer_status)

    init {
        setupGraph()

        inflate(context, R.layout.show_mode_view_item, this)

        orientation = VERTICAL
        gravity = Gravity.CENTER

        setOnClickListener { presenter.onClickView() }

        setBackgroundResource(R.drawable.show_mode_view_item_bg)
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
        drawable.setColorFilter(
            ColorUtils.getColor(context, timer.colorId),
            PorterDuff.Mode.SRC_ATOP,
        )

        chrono.text = TimeConverter.convertSecondsToHumanTime(timer.time.toLong())
        timerName.text = timer.name
    }

    private fun createScreen() = object : ShowModeItemViewContract.Screen {
        override fun statusUpdated(activate: Boolean) {
            if (activate) {
                timerState.setImageResource(R.drawable.show_mode_item_view_pause)
            } else {
                timerState.setImageResource(R.drawable.show_mode_item_view_play)
            }
        }

        override fun timerUpdated(newTime: Long) {
            chrono.text = TimeConverter.convertSecondsToHumanTime(newTime)
        }
    }

    private fun setupGraph() {
        val component = DaggerShowModeItemViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .showModeItemViewModule(ShowModeItemViewModule(screen))
            .build()
        component.inject(this)
    }
}
