package fr.bowser.behaviortracker.floating_running_timer_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.update_timer_time_view.UpdateTimerTimeDialog
import fr.bowser.behaviortracker.utils.ViewExtension.bind
import javax.inject.Inject

class FloatingRunningTimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var presenter: FloatingRunningTimerViewContract.Presenter

    private val screen = createScreen()

    private val playPauseBtn: ImageView by bind(R.id.floating_running_view_timer_state)
    private val currentTimeTv: TextView by bind(R.id.floating_running_view_timer_time)
    private val timerNameTv: TextView by bind(R.id.floating_running_view_timer_name)

    init {
        setupGraph()

        inflate(context, R.layout.floating_running_timer_view, this)
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))

        setOnClickListener { presenter.onClickView() }

        playPauseBtn.setOnClickListener { presenter.onClickPlayPause() }
        findViewById<View>(R.id.floating_running_view_update_time).setOnClickListener {
            presenter.onClickUpdateTime()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onStart()
    }

    override fun onDetachedFromWindow() {
        presenter.onStop()
        super.onDetachedFromWindow()
    }

    private fun setupGraph() {
        val component = DaggerFloatingRunningTimerViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .floatingRunningTimerViewModule(FloatingRunningTimerViewModule(screen))
            .build()
        component.inject(this)
    }

    private fun createScreen() = object : FloatingRunningTimerViewContract.Screen {

        override fun displayUpdateTimer(timerId: Long) {
            val updateTimerTimeDialog = UpdateTimerTimeDialog.newInstance(timerId)
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            updateTimerTimeDialog.show(fragmentManager, UpdateTimerTimeDialog.TAG)
        }

        override fun displayView(display: Boolean) {
            visibility = if (display) View.VISIBLE else View.GONE
        }

        override fun updateTimer(timerName: String) {
            timerNameTv.text = timerName
        }

        override fun changeState(isPlaying: Boolean) {
            playPauseBtn.setImageResource(
                if (isPlaying) {
                    R.drawable.floating_running_timer_view_pause
                } else {
                    R.drawable.floating_running_timer_view_play
                },
            )
        }

        override fun updateTime(time: String) {
            currentTimeTv.text = time
        }
    }
}
