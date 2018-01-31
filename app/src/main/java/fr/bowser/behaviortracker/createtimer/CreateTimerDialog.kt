package fr.bowser.behaviortracker.createtimer

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject




class CreateTimerDialog : DialogFragment(), CreateTimerContract.View {

    @Inject
    lateinit var presenter: CreateTimerPresenter

    private lateinit var editTimerName: EditText
    private lateinit var chooseColor: Spinner
    private lateinit var startNow: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_create_timer, container, false)
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

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun exitViewAfterSucceedTimerCreation() {
        exitWithAnimation()
    }

    private fun initSpinner(root: View) {
        chooseColor = root.findViewById(R.id.choose_color)
        val colorAdapter = ColorAdapter(context, android.R.layout.simple_spinner_item, provideColors())
        chooseColor.adapter = colorAdapter
    }

    private fun provideColors(): MutableList<Int>? {
        val colors = ArrayList<Int>()
        colors.add(Color.RED)
        colors.add(Color.BLUE)
        colors.add(Color.YELLOW)
        colors.add(Color.GREEN)
        colors.add(Color.CYAN)
        return colors
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
        myToolbar.setNavigationIcon(R.drawable.ic_close_black)
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
    }

    private fun displayKeyboard(){
        editTimerName.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editTimerName.windowToken, 0)
    }

    private fun exitWithAnimation(){
        activity?.supportFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                ?.remove(this)
                ?.commit()
    }

    private fun saveTimer() {
        val timerName = editTimerName.text.toString()
        val color: Int = chooseColor.adapter.getItem(chooseColor.selectedItemPosition) as Int

        presenter.createTimer(timerName, color, startNow.isChecked)
    }

    companion object {
        const val TAG = "CreateTimerActivity"

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