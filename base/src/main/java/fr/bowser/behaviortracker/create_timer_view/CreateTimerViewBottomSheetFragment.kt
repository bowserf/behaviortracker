package fr.bowser.behaviortracker.create_timer_view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethod.SHOW_FORCED
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.speech_to_text.internal.SpeechToTextManagerImpl
import fr.bowser.behaviortracker.utils.setMultiLineCapSentencesAndDoneAction
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateTimerViewBottomSheetFragment : BottomSheetDialogFragment(R.layout.create_timer_view) {

    @Inject
    lateinit var presenter: CreateTimerViewContract.Presenter

    private val screen = createScreen()

    private val imm by lazy {
        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private lateinit var editTimerName: EditText
    private lateinit var chooseColor: RecyclerView
    private lateinit var startNow: CheckBox
    private lateinit var editTimerNameLayout: TextInputLayout
    private lateinit var timerPicker: TimePicker

    private lateinit var timeStateImg: ImageView
    private lateinit var colorStateImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isPomodoro = requireArguments().getBoolean(IS_POMODORO)
        presenter.enablePomodoroMode(isPomodoro)

        initColorList(view)
        initTimePicker(view)
        initUI(view, isPomodoro)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    private fun initColorList(root: View) {
        chooseColor = root.findViewById(R.id.list_colors)
        chooseColor.layoutManager = GridLayoutManager(
            activity,
            resources.getInteger(R.integer.create_timer_number_colors_row),
            RecyclerView.VERTICAL,
            false,
        )
    }

    private fun initTimePicker(root: View) {
        timerPicker = root.findViewById(R.id.create_timer_time_picker)
        timerPicker.setIs24HourView(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timerPicker.hour = 0
            timerPicker.minute = 0
        } else {
            timerPicker.currentHour = 0
            timerPicker.currentMinute = 0
        }
    }

    private fun setupGraph() {
        val component = DaggerCreateTimerViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .createTimerViewModule(CreateTimerViewModule(screen))
            .build()
        component.inject(this)
    }

    private fun initUI(root: View, isPomodoro: Boolean) {
        editTimerName = root.findViewById(R.id.creation_timer_name)
        editTimerName.setMultiLineCapSentencesAndDoneAction()

        // methods to display keyboard and focus edittext
        displayKeyboard()

        startNow = root.findViewById(R.id.start_after_creation)
        if (isPomodoro) {
            startNow.visibility = View.GONE
        }

        root.findViewById<View>(R.id.create_timer_color_container)
            .setOnClickListener { presenter.onClickChangeColorState() }
        root.findViewById<View>(R.id.create_timer_time_container)
            .setOnClickListener { presenter.onClickChangeTimeState() }
        timeStateImg = root.findViewById(R.id.create_timer_time_container_status)
        colorStateImg = root.findViewById(R.id.create_timer_color_container_status)

        editTimerNameLayout = root.findViewById(R.id.creation_timer_name_layout)
        editTimerNameLayout.setStartIconOnClickListener {
            presenter.onClickMic(this)
        }

        root.findViewById<View>(R.id.create_timer_view_create).setOnClickListener { saveTimer() }

        root.findViewById<View>(R.id.create_timer_close).setOnClickListener { dismiss() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SpeechToTextManagerImpl.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onSpeechToTextDataRetrieved(data)
            }
        }
    }

    private fun displayKeyboard() {
        editTimerName.requestFocus()
        lifecycleScope.launch {
            delay(200)
            imm.showSoftInput(editTimerName, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun saveTimer() {
        val hour: Int
        val minute: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timerPicker.hour
            minute = timerPicker.minute
        } else {
            hour = timerPicker.currentHour
            minute = timerPicker.currentMinute
        }
        val timerName = editTimerName.text.toString()
        presenter.onClickCreateTimer(timerName, hour, minute, startNow.isChecked)
    }

    private fun createScreen() = object : CreateTimerViewContract.Screen {
        override fun exitViewAfterSucceedTimerCreation() {
            dismiss()
        }

        override fun displayNameError() {
            editTimerNameLayout.error = resources.getString(R.string.create_timer_name_error)
        }

        override fun updateColorList(oldSelectedPosition: Int, selectedPosition: Int) {
            chooseColor.adapter?.notifyItemChanged(oldSelectedPosition)
            chooseColor.adapter?.notifyItemChanged(selectedPosition)
        }

        override fun fillColorList(colorPosition: Int) {
            chooseColor.adapter = CreateTimerViewColorAdapter(
                requireContext(),
                colorPosition,
                object : CreateTimerViewColorAdapter.Callback {
                    override fun onChangeSelectedColor(
                        oldSelectedPosition: Int,
                        selectedPosition: Int,
                    ) {
                        presenter.onClickColor(oldSelectedPosition, selectedPosition)
                    }
                },
            )
        }

        override fun updateContainerTimeState(isDisplay: Boolean) {
            timerPicker.visibility = if (isDisplay) View.VISIBLE else View.GONE
            timeStateImg.setImageResource(
                if (isDisplay) {
                    R.drawable.create_timer_view_content_show
                } else {
                    R.drawable.create_timer_view_content_hidden
                },
            )
        }

        override fun updateContainerColorState(isDisplay: Boolean) {
            chooseColor.visibility = if (isDisplay) {
                View.VISIBLE
            } else {
                View.GONE
            }
            colorStateImg.setImageResource(
                if (isDisplay) {
                    R.drawable.create_timer_view_content_show
                } else {
                    R.drawable.create_timer_view_content_hidden
                },
            )
        }

        override fun setTimerName(text: String) {
            editTimerName.setText(text)
            editTimerName.setSelection(editTimerName.length())
        }

        override fun showSpeechIcon(isVisible: Boolean) {
            editTimerNameLayout.isStartIconVisible = isVisible
        }

        override fun showSpeechToTextError() {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.create_timer_speech_to_text_error),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    companion object {
        private const val TAG = "CreateTimerActivity"

        private const val IS_POMODORO = "create_timer_dialog.extra.is_pomodoro"

        fun showDialog(
            activity: AppCompatActivity,
            isPomodoro: Boolean,
        ) {
            val createTimerDialog = CreateTimerViewBottomSheetFragment()

            val bundle = Bundle()
            bundle.putBoolean(IS_POMODORO, isPomodoro)
            createTimerDialog.arguments = bundle

            createTimerDialog.show(activity.supportFragmentManager, TAG)
        }
    }
}
