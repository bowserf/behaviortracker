package fr.bowser.behaviortracker.create_timer_view

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject

class CreateTimerDialog : DialogFragment(R.layout.fragment_create_timer) {

    @Inject
    lateinit var presenter: CreateTimerContract.Presenter

    private val screen = createScreen()

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isPomodoro = requireArguments().getBoolean(IS_POMODORO)
        presenter.enablePomodoroMode(isPomodoro)

        initToolbar(view)
        initColorList(view)
        initTimePicker(view)
        initUI(view, isPomodoro)
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
        hideKeyboard()
        presenter.onStop()
        super.onStop()
    }

    private fun initColorList(root: View) {
        chooseColor = root.findViewById(R.id.list_colors)
        chooseColor.layoutManager = GridLayoutManager(
            activity,
            resources.getInteger(R.integer.create_timer_number_colors_row),
            RecyclerView.VERTICAL,
            false
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
        val component = DaggerCreateTimerComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .createTimerModule(CreateTimerModule(screen))
            .build()
        component.inject(this)
    }

    private fun initToolbar(root: View) {
        val myToolbar = root.findViewById<Toolbar>(R.id.create_timer_toolbar)
        myToolbar.title = resources.getString(R.string.create_timer_title)
        myToolbar.setNavigationIcon(R.drawable.ic_close)
        myToolbar.setNavigationOnClickListener {
            exitWithAnimation()
        }
        myToolbar.inflateMenu(R.menu.menu_create_timer)
        myToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_create_timer_save) {
                saveTimer()
            }
            true
        }
    }

    private fun initUI(root: View, isPomodoro: Boolean) {
        editTimerName = root.findViewById(R.id.creation_timer_name)

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
    }

    private fun displayKeyboard() {
        editTimerName.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editTimerName.windowToken, 0)
    }

    private fun exitWithAnimation() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            ?.remove(this)
            ?.commit()
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

    private fun createScreen() = object : CreateTimerContract.Screen {
        override fun exitViewAfterSucceedTimerCreation() {
            exitWithAnimation()
        }

        override fun displayNameError() {
            editTimerNameLayout.error = resources.getString(R.string.create_timer_name_error)
        }

        override fun updateColorList(oldSelectedPosition: Int, selectedPosition: Int) {
            chooseColor.adapter?.notifyItemChanged(oldSelectedPosition)
            chooseColor.adapter?.notifyItemChanged(selectedPosition)
        }

        override fun fillColorList(colorPosition: Int) {
            chooseColor.adapter = ColorAdapter(
                requireContext(),
                colorPosition,
                object : ColorAdapter.Callback {
                    override fun onChangeSelectedColor(
                        oldSelectedPosition: Int,
                        selectedPosition: Int
                    ) {
                        presenter.onClickColor(oldSelectedPosition, selectedPosition)
                    }
                }
            )
        }

        override fun updateContainerTimeState(isDisplay: Boolean) {
            timerPicker.visibility = if (isDisplay) View.VISIBLE else View.GONE
            timeStateImg.setImageResource(if (isDisplay) R.drawable.create_timer_content_show else R.drawable.create_timer_content_hidden)
        }

        override fun updateContainerColorState(isDisplay: Boolean) {
            chooseColor.visibility = if (isDisplay) View.VISIBLE else View.GONE
            colorStateImg.setImageResource(if (isDisplay) R.drawable.create_timer_content_show else R.drawable.create_timer_content_hidden)
        }
    }

    companion object {
        private const val TAG = "CreateTimerActivity"

        private const val IS_POMODORO = "create_timer_dialog.extra.is_pomodoro"

        fun showDialog(
            activity: AppCompatActivity,
            isPomodoro: Boolean,
            isLargeScreen: Boolean = true
        ) {
            val createTimerDialog = CreateTimerDialog()

            val bundle = Bundle()
            bundle.putBoolean(IS_POMODORO, isPomodoro)
            createTimerDialog.arguments = bundle

            if (isLargeScreen) {
                // The device is using a large layout, so show the fragment as a dialog
                createTimerDialog.show(activity.supportFragmentManager, TAG)
            } else {
                // The device is smaller, so show the fragment fullscreen
                activity.supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(android.R.id.content, createTimerDialog)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
