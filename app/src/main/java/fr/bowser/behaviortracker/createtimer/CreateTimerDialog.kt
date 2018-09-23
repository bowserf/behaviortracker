package fr.bowser.behaviortracker.createtimer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
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

class CreateTimerDialog : DialogFragment(), CreateTimerContract.View {

    @Inject
    lateinit var presenter: CreateTimerPresenter

    private lateinit var editTimerName: EditText
    private lateinit var chooseColor: RecyclerView
    private lateinit var startNow: CheckBox
    private lateinit var editTimerNameLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_timer, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(view)
        initSpinner(view)
        initUI(view)
    }

    override fun onStart() {
        super.onStart()

        if (dialog == null) {
            return
        }

        val dialogWidth = resources.getDimensionPixelOffset(R.dimen.create_dialog_width)
        dialog.window.setLayout(dialogWidth, WRAP_CONTENT)
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

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

    private fun initSpinner(root: View) {
        chooseColor = root.findViewById(R.id.list_colors)
        chooseColor.layoutManager = GridLayoutManager(activity,
                resources.getInteger(R.integer.create_timer_number_colors_row),
                GridLayoutManager.VERTICAL,
                false)
        chooseColor.setHasFixedSize(true)
        chooseColor.adapter = ColorAdapter(context!!, presenter)
    }

    private fun setupGraph() {
        val component = DaggerCreateTimerComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .createTimerModule(CreateTimerModule(this))
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

    private fun initUI(root: View) {
        editTimerName = root.findViewById(R.id.creation_timer_name)

        // methods to display keyboard and dselect edittext
        displayKeyboard()

        startNow = root.findViewById(R.id.start_after_creation)

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
        val timerName = editTimerName.text.toString()
        presenter.createTimer(timerName, startNow.isChecked)
    }

    companion object {
        private const val TAG = "CreateTimerActivity"

        fun showDialog(activity: AppCompatActivity, isLargeScreen: Boolean) {
            val newFragment = CreateTimerDialog()
            if (isLargeScreen) {
                // The device is using a large layout, so show the fragment as a dialog
                newFragment.show(activity.supportFragmentManager, TAG)
            } else {
                // The device is smaller, so show the fragment fullscreen
                activity.supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(android.R.id.content, newFragment)
                        .addToBackStack(null)
                        .commit()
            }

        }
    }


}