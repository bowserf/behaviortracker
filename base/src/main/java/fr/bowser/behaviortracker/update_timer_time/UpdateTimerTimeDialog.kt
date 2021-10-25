package fr.bowser.behaviortracker.update_timer_time

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject

class UpdateTimerTimeDialog : DialogFragment() {

    @Inject
    lateinit var presenter: UpdateTimerTimeContract.Presenter

    private val screen = createScreen()

    private lateinit var timerPicker: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val timerId = requireArguments().getLong(UPDATE_TIMER_TIME_DIALOG_EXTRA_TIMER_ID)
        setupGraph(timerId)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = LayoutInflater.from(context).inflate(R.layout.update_timer_time_dialog, null)

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(resources.getString(R.string.update_timer_time_dialog_title))
        dialogBuilder.setView(root)
        dialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
            val hour: Int
            val minute: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = timerPicker.hour
                minute = timerPicker.minute
            } else {
                hour = timerPicker.currentHour
                minute = timerPicker.currentMinute
            }
            presenter.onClickValidate(hour, minute)
        }
        dialogBuilder.setNegativeButton(android.R.string.cancel, null)

        timerPicker = root.findViewById(R.id.update_timer_time_picker)
        timerPicker.setIs24HourView(true)

        return dialogBuilder.create()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    private fun setupGraph(timerId: Long) {
        val component = DaggerUpdateTimerTimeComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .updateTimerTimeModule(UpdateTimerTimeModule(screen, timerId))
            .build()
        component.inject(this)
    }

    private fun createScreen() = object : UpdateTimerTimeContract.Screen {
        override fun displayTimerInformation(currentHour: Int, currentMinute: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timerPicker.hour = currentHour
                timerPicker.minute = currentMinute
            } else {
                timerPicker.currentHour = currentHour
                timerPicker.currentMinute = currentMinute
            }
        }
    }

    companion object {

        const val TAG = "UpdateTimerTimeDialog"

        private const val UPDATE_TIMER_TIME_DIALOG_EXTRA_TIMER_ID = "update_timer_time_dialog.extra.timer_id"

        fun newInstance(timerId: Long): UpdateTimerTimeDialog {
            val bundle = Bundle()
            bundle.putLong(UPDATE_TIMER_TIME_DIALOG_EXTRA_TIMER_ID, timerId)
            val fragment = UpdateTimerTimeDialog()
            fragment.arguments = bundle
            return fragment
        }
    }
}