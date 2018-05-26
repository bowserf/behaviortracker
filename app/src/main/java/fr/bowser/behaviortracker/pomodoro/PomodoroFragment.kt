package fr.bowser.behaviortracker.pomodoro

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject

class PomodoroFragment : Fragment(), PomodoroContract.View {

    @Inject
    lateinit var presenter: PomodoroPresenter

    private lateinit var timer: TextView
    private lateinit var timerBtn: View
    private lateinit var spinnerAction: Spinner
    private lateinit var spinnerActionRest: Spinner
    private lateinit var timerStatus: ImageView
    private lateinit var actionContainer: View
    private lateinit var restContainer: View

    private var notDisplayCreateTimer = false

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

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    override fun populateSpinnerAction(actions: MutableList<String>) {
        actions.add(resources.getString(R.string.pomodoro_create_timer))
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, actions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAction.adapter = adapter

        if(actions.size == 1){
            notDisplayCreateTimer = true
        }
    }

    override fun populateSpinnerRestAction(actions: MutableList<String>) {
        actions.add(0, resources.getString(R.string.pomodoro_no_rest_timer))
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, actions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerActionRest.adapter = adapter
    }

    override fun startCurrentAction() {
        timerStatus.setImageResource(R.drawable.ic_pause)
    }

    override fun pauseCurrentAction() {
        timerStatus.setImageResource(R.drawable.ic_play)
    }

    override fun updatePomodoroTime(currentTimer: Timer?, pomodoroTime: Long) {
        timer.text = TimeConverter.convertSecondsToHumanTime(pomodoroTime, false)
    }

    override fun displayColorOfSelectedRestTimer(colorIndex: Int) {
        val drawable = restContainer.background as GradientDrawable
        val color = ColorUtils.getColor(context!!, colorIndex)
        drawable.setColor(color)
        drawable.setStroke(resources.getDimensionPixelOffset(R.dimen.pomodoro_stroke_width), color)
    }

    override fun displayColorOfSelectedActionTimer(colorIndex: Int) {
        val drawable = actionContainer.background as GradientDrawable
        val color = ColorUtils.getColor(context!!, colorIndex)
        drawable.setColor(color)
        drawable.setStroke(resources.getDimensionPixelOffset(R.dimen.pomodoro_stroke_width), color)
    }

    override fun displayActionColorTimer(colorIndex: Int) {
        val drawable = timerBtn.background as GradientDrawable
        val color = ColorUtils.getColor(context!!, colorIndex)
        drawable.setColor(color)
        drawable.setStroke(resources.getDimensionPixelOffset(R.dimen.pomodoro_stroke_width), color)
    }

    override fun displayColorNoAction() {
        val drawable = actionContainer.background as GradientDrawable
        drawable.setColor(ContextCompat.getColor(context!!, android.R.color.transparent))
        drawable.setStroke(
                resources.getDimensionPixelOffset(R.dimen.pomodoro_stroke_width),
                ContextCompat.getColor(context!!, R.color.grey))
    }

    override fun displayColorNoRest() {
        val drawable = restContainer.background as GradientDrawable
        drawable.setColor(ContextCompat.getColor(context!!, android.R.color.transparent))
        drawable.setStroke(
                resources.getDimensionPixelOffset(R.dimen.pomodoro_stroke_width),
                ContextCompat.getColor(context!!, R.color.grey))
    }

    override fun createTimer() {
        if(notDisplayCreateTimer){
            notDisplayCreateTimer = false
            return
        }
        CreateTimerDialog.showDialog(activity!!)
    }

    override fun selectActionTimer(positionNewTimer: Int) {
        spinnerAction.setSelection(positionNewTimer)
    }

    private fun setupGraph() {
        val builder = DaggerPomodoroComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .pomodoroModule(PomodoroModule(this))
                .build()
        builder.inject(this)
    }

    private fun initUI(view: View) {
        actionContainer = view.findViewById(R.id.pomodoro_container_action)
        restContainer = view.findViewById(R.id.pomodoro_container_rest)

        timer = view.findViewById(R.id.pomodoro_timer)
        timerStatus = view.findViewById(R.id.pomodoro_timer_status)
        timerBtn = view.findViewById(R.id.pomodoro_timer_bg)
        spinnerAction = view.findViewById(R.id.pomodoro_spinner_action)
        spinnerActionRest = view.findViewById(R.id.pomodoro_spinner_action_rest)

        spinnerAction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // nothing to do
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.onItemSelectedForAction(position)
            }
        }

        spinnerActionRest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // nothing to do
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.onItemSelectedForRest(position)
            }
        }

        timerBtn.setOnClickListener {
            presenter.onChangePomodoroStatus(
                    spinnerAction.selectedItemPosition, spinnerActionRest.selectedItemPosition)
        }
    }

    companion object {
        const val TAG = "PomodoroFragment"
    }

}