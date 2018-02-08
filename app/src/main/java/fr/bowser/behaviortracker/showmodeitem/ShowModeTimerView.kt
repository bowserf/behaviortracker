package fr.bowser.behaviortracker.showmodeitem

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject

class ShowModeTimerView(context: Context) : ConstraintLayout(context),
        ShowModeTimerViewContract.View {

    @Inject
    lateinit var presenter: ShowModeTimerViewPresenter

    private var chrono: TextView
    private var timerName: TextView

    init {
        setupGraph()

        inflate(context, R.layout.item_show_mode, this)

        setOnClickListener { presenter.onClickView() }

        chrono = findViewById(R.id.show_mode_timer_time)
        timerName = findViewById(R.id.show_mode_timer_name)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.start()
    }

    override fun onDetachedFromWindow() {
        presenter.stop()
        super.onDetachedFromWindow()
    }

    override fun timerUpdated(newTime: Long) {
        chrono.text = TimeConverter.convertSecondsToHumanTime(newTime)
    }

    fun setTimer(timer: Timer) {
        presenter.setTimer(timer)

        setBackgroundColor(timer.color)

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