package fr.bowser.behaviortracker.alarm_view

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.feature.alarm.AlarmTime
import javax.inject.Inject

class AlarmViewDialog : DialogFragment() {

    @Inject
    lateinit var presenter: AlarmViewContract.Presenter

    private val screen = createScreen()

    private lateinit var timerPicker: TimePicker

    private lateinit var alarmStatus: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = requireActivity().layoutInflater.inflate(R.layout.alarm_view, null)

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setTitle(resources.getString(R.string.alarm_timer_dialog_title))
        dialogBuilder.setMessage(resources.getString(R.string.alarm_timer_dialog_content))
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
            val alarmTime = AlarmTime(hour, minute, alarmStatus.isChecked)
            presenter.onClickValidate(alarmTime)
        }
        dialogBuilder.setNegativeButton(android.R.string.cancel, null)

        alarmStatus = root.findViewById(R.id.alarm_view_status)
        timerPicker = root.findViewById(R.id.alarm_view_time_picker)
        timerPicker.setIs24HourView(true)

        return dialogBuilder.create()
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
        val component = DaggerAlarmViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .alarmViewModule(AlarmViewModule(screen))
            .build()
        component.inject(this)
    }

    private fun createScreen() = object : AlarmViewContract.Screen {
        @Suppress("DEPRECATION")
        override fun restoreAlarmStatus(alarmTime: AlarmTime) {
            alarmStatus.isChecked = alarmTime.isActivated
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timerPicker.hour = alarmTime.hour
                timerPicker.minute = alarmTime.minute
            } else {
                timerPicker.currentHour = alarmTime.hour
                timerPicker.currentMinute = alarmTime.minute
            }
        }

        override fun displayMessageAlarmEnabled() {
            Toast.makeText(context, R.string.alarm_timer_alarm_enabled, Toast.LENGTH_SHORT).show()
        }

        override fun displayMessageAlarmDisabled() {
            Toast.makeText(context, R.string.alarm_timer_alarm_disabled, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        const val TAG = "AlarmTimerDialog"

        fun newInstance(): AlarmViewDialog {
            return AlarmViewDialog()
        }
    }
}
