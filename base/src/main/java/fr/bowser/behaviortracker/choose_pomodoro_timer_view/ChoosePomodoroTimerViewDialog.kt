package fr.bowser.behaviortracker.choose_pomodoro_timer_view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.FragmentExtension.bind
import javax.inject.Inject

class ChoosePomodoroTimerViewDialog : DialogFragment(R.layout.choose_pomodoro_timer_view) {

    @Inject
    lateinit var presenter: ChoosePomodoroTimerViewContract.Presenter

    private val screen = createScreen()

    private val timerList: RecyclerView by bind(R.id.choose_pomodoro_timer_view_timers)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(view)

        timerList.layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        )
        timerList.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()

        if (dialog == null) {
            return
        }

        val dialogWidth = resources.getDimensionPixelOffset(R.dimen.create_dialog_width)
        dialog!!.window!!.setLayout(dialogWidth, WRAP_CONTENT)

        presenter.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    private fun setupGraph() {
        val component = DaggerChoosePomodoroTimerViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .choosePomodoroTimerViewModule(ChoosePomodoroTimerViewModule(screen))
            .build()
        component.inject(this)
    }

    private fun initToolbar(root: View) {
        val myToolbar = root.findViewById<Toolbar>(R.id.choose_pomodoro_timer_view_toolbar)
        myToolbar.title = resources.getString(R.string.choose_pomodoro_timer_view_title)
        myToolbar.setNavigationIcon(R.drawable.create_timer_view_close)
        myToolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun createScreen() = object : ChoosePomodoroTimerViewContract.Screen {
        override fun displayTimerList(timerList: List<Timer>) {
            this@ChoosePomodoroTimerViewDialog.timerList.adapter = ChoosePomodoroTimerViewAdapter(
                timerList,
                object : ChoosePomodoroTimerViewAdapter.Listener {
                    override fun onTimerChose(timer: Timer) {
                        presenter.onTimerChose(timer)
                        dismiss()
                    }
                }
            )
        }
    }

    companion object {
        const val TAG = "CreateTimerActivity"

        fun newInstance(): ChoosePomodoroTimerViewDialog {
            return ChoosePomodoroTimerViewDialog()
        }
    }
}
