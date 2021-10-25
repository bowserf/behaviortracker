package fr.bowser.behaviortracker.choosepomodorotimer

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import javax.inject.Inject

class ChoosePomodoroTimerDialog : DialogFragment(R.layout.fragment_choose_timer) {

    @Inject
    lateinit var presenter: ChoosePomodoroTimerContract.Presenter

    private val screen = createScreen()

    private val timerList by lazy { requireView().findViewById<RecyclerView>(R.id.timers_list) }

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

    private fun setupGraph() {
        val component = DaggerChoosePomodoroTimerComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .choosePomodoroTimerModule(ChoosePomodoroTimerModule(screen))
            .build()
        component.inject(this)
    }

    private fun initToolbar(root: View) {
        val myToolbar = root.findViewById<Toolbar>(R.id.timers_list_toolbar)
        myToolbar.title = resources.getString(R.string.pomodoro_choose_timer_title)
        myToolbar.setNavigationIcon(R.drawable.ic_close)
        myToolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun createScreen() = object : ChoosePomodoroTimerContract.Screen {
        override fun displayTimerList(timerList: List<Timer>) {
            this@ChoosePomodoroTimerDialog.timerList.adapter = ChoosePomodoroTimerAdapter(
                requireContext(),
                timerList,
                object : ChoosePomodoroTimerAdapter.Listener {
                    override fun onTimerChose(timer: Timer) {
                        presenter.onTimerChose(timer)
                        dismiss()
                    }
                })
        }
    }

    companion object {
        private const val TAG = "CreateTimerActivity"

        fun showDialog(activity: AppCompatActivity) {
            val newFragment = ChoosePomodoroTimerDialog()
            newFragment.show(activity.supportFragmentManager, TAG)
        }
    }
}