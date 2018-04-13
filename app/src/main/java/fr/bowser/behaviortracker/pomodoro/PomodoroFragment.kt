package fr.bowser.behaviortracker.pomodoro

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject

class PomodoroFragment : Fragment(), PomodoroContract.View {

    @Inject
    lateinit var presenter: PomodoroPresenter

    private lateinit var timer: TextView
    private lateinit var resetTimer: ImageView
    private lateinit var manageStatus: FloatingActionButton
    private lateinit var spinnerAction: Spinner
    private lateinit var spinnerActionRest: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
    }

    private fun setupGraph() {
        val builder = DaggerPomodoroComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .pomodoroModule(PomodoroModule(this))
                .build()
        builder.inject(this)
    }

    private fun initUI(view: View) {
        timer = view.findViewById(R.id.pomodoro_timer)
        resetTimer = view.findViewById(R.id.pomodoro_reset_timer)
        manageStatus = view.findViewById(R.id.pomodoro_button_manage_status)
        spinnerAction = view.findViewById(R.id.pomodoro_spinner_action)
        spinnerActionRest = view.findViewById(R.id.pomodoro_spinner_action_rest)
    }

    companion object {
        const val TAG = "PomodoroFragment"
    }

}