package fr.bowser.behaviortracker.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import javax.inject.Inject


class PomodoroFragment : Fragment(), PomodoroContract.Screen {

    @Inject
    lateinit var presenter: PomodoroPresenter

    private lateinit var defaultImage: ImageView
    private lateinit var description: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var currentTime: TextView
    private lateinit var activeTimer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = view.findViewById(R.id.pomodoro_choose_timer)
        defaultImage = view.findViewById(R.id.pomodoro_default_image)
        description = view.findViewById(R.id.pomodoro_description)
        currentTime = view.findViewById(R.id.pomodoro_current_time)
        activeTimer = view.findViewById(R.id.pomodoro_active_timer)

        fab.setOnClickListener { presenter.onClickFab() }
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    override fun updatePomodoroTime(timer: Timer, currentTime: Long) {
        this.currentTime.text = currentTime.toString()
    }

    override fun updatePomodoroTimer(timer: Timer, duration: Long) {
        defaultImage.visibility = View.GONE
        description.visibility = View.GONE
        currentTime.visibility = View.VISIBLE
        activeTimer.visibility = View.VISIBLE

        this.activeTimer.text = timer.name
        this.currentTime.text = duration.toString()
    }

    override fun getPauseTimer(): Timer {
        return Timer(resources.getString(R.string.pomodoro_timer_pause), ColorUtils.COLOR_BLUE_GREY)
    }

    private fun setupGraph() {
        val build = DaggerPomodoroComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .pomodoroModule(PomodoroModule(this))
                .build()
        build.inject(this)
    }

    companion object {
        const val TAG = "PomodoroFragment"
    }

}