package fr.bowser.behaviortracker.pomodoro_choose_timer_view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.FragmentExtension.bind
import javax.inject.Inject

class PomodoroChooseTimerViewDialog :
    BottomSheetDialogFragment(R.layout.choose_pomodoro_timer_view) {

    @Inject
    lateinit var presenter: PomodoroChooseTimerViewContract.Presenter

    private val screen = createScreen()

    private val timerList: RecyclerView by bind(R.id.choose_pomodoro_timer_view_timers)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerList.layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false,
        )
        timerList.setHasFixedSize(true)

        view.findViewById<View>(R.id.choose_pomodoro_timer_view_close)
            .setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    private fun setupGraph() {
        val component = DaggerPomodoroChooseTimerViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .pomodoroChooseTimerViewModule(PomodoroChooseTimerViewModule(screen))
            .build()
        component.inject(this)
    }

    private fun createScreen() = object : PomodoroChooseTimerViewContract.Screen {
        override fun displayTimerList(timerList: List<Timer>) {
            this@PomodoroChooseTimerViewDialog.timerList.adapter = PomodoroChooseTimerViewAdapter(
                timerList,
                object : PomodoroChooseTimerViewAdapter.Listener {
                    override fun onTimerChose(timer: Timer) {
                        presenter.onTimerChose(timer)
                        dismiss()
                    }
                },
            )
        }
    }

    companion object {
        private const val TAG = "CreateTimerActivity"

        fun showDialog(
            activity: AppCompatActivity,
        ) {
            val pomodoroChooseTimerViewDialog = PomodoroChooseTimerViewDialog()
            pomodoroChooseTimerViewDialog.show(
                activity.supportFragmentManager,
                TAG,
            )
        }
    }
}
