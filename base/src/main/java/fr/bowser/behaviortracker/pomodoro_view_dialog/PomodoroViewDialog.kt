package fr.bowser.behaviortracker.pomodoro_view_dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject

class PomodoroViewDialog : DialogFragment() {

    @Inject
    lateinit var presenter: PomodoroViewDialogContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setTitle(resources.getString(R.string.pomodoro_dialog_title))
        dialogBuilder.setMessage(resources.getString(R.string.pomodoro_dialog_content))
        dialogBuilder.setPositiveButton(R.string.pomodoro_dialog_continue) { _, _ ->
            presenter.onClickPositionButton()
        }
        dialogBuilder.setNegativeButton(R.string.pomodoro_dialog_stop) { _, _ ->
            presenter.onClickNegativeButton()
        }

        val dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun setupGraph() {
        val component = DaggerPomodoroViewDialogComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .pomodoroViewDialogModule(PomodoroViewDialogModule())
            .build()
        component.inject(this)
    }

    companion object {

        const val TAG = "PomodoroDialog"

        fun newInstance(): PomodoroViewDialog {
            return PomodoroViewDialog()
        }
    }
}
