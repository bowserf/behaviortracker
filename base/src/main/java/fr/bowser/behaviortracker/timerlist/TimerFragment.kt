package fr.bowser.behaviortracker.timerlist

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.alarm.AlarmTimerDialog
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import javax.inject.Inject

class TimerFragment : Fragment(), TimerContract.Screen {

    @Inject
    lateinit var presenter: TimerPresenter

    private lateinit var fab: FloatingActionButton

    private lateinit var emptyListView: ImageView

    private lateinit var emptyListText: TextView

    private lateinit var timerListSectionAdapter: TimerListSectionAdapterDelegate

    private lateinit var timerList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        setupGraph()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_timer, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeList(view)

        fab = view.findViewById(R.id.button_add_timer)
        fab.setOnClickListener { presenter.onClickAddTimer() }

        emptyListView = view.findViewById(R.id.empty_list_view)
        emptyListText = view.findViewById(R.id.empty_list_text)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)!!
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        presenter.init()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!presenter.isInstantApp()) {
            inflater.inflate(R.menu.menu_home, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_reset_all -> {
                presenter.onClickResetAll()
                return true
            }
            R.id.menu_remove_all -> {
                presenter.onClickRemoveAllTimers()
                return true
            }
            R.id.menu_settings -> {
                presenter.onClickSettings()
                return true
            }
            R.id.menu_alarm -> {
                presenter.onClickAlarm()
                return true
            }
            R.id.menu_rewards -> {
                presenter.onClickRewards()
                return true
            }
        }
        return false
    }

    override fun displayResetAllDialog() {
        val message = resources.getString(R.string.home_dialog_confirm_reset_all_timers)
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                presenter.onClickResetAllTimers()
            }
            .setNegativeButton(android.R.string.no) { dialog, which ->
                // do nothing
            }
            .show()
    }

    override fun displaySettingsView() {
        findNavController().navigate(R.id.settings_screen)
    }

    override fun displayAlarmTimerDialog() {
        val alertDialog = AlarmTimerDialog.newInstance()
        alertDialog.show(childFragmentManager, AlarmTimerDialog.TAG)
    }

    override fun displayRewardsView() {
        findNavController().navigate(R.id.rewards_screen)
    }

    override fun displayRemoveAllTimersConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.timer_list_remove_all_timers_title))
            .setMessage(resources.getString(R.string.timer_list_remove_all_timers_message))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                presenter.onClickConfirmRemoveAllTimers()
            }
            .setNegativeButton(android.R.string.cancel, null)
        dialogBuilder.show()
    }

    override fun displayCreateTimerView() {
        CreateTimerDialog.showDialog(activity as AppCompatActivity, false)
    }

    override fun displayTimerListSections(sections: List<TimerListSection>) {
        timerListSectionAdapter.populate(sections)
    }

    override fun displayEmptyListView() {
        timerList.visibility = INVISIBLE
        emptyListView.visibility = VISIBLE
        emptyListText.visibility = VISIBLE

        val fabAnimator = ObjectAnimator.ofFloat(this, PROPERTY_FAB_ANIMATION, 1f, 1.15f, 1f)
        fabAnimator.duration = FAB_ANIMATION_DURATION
        fabAnimator.repeatCount = 1
        fabAnimator.interpolator = AccelerateDecelerateInterpolator()
        fabAnimator.startDelay = FAB_ANIMATION_DELAY
        fabAnimator.start()
    }

    override fun displayListView() {
        timerList.visibility = VISIBLE
        emptyListView.visibility = INVISIBLE
        emptyListText.visibility = INVISIBLE
    }

    private fun setupGraph() {
        val build = DaggerTimerComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .timerModule(TimerModule(this))
            .build()
        build.inject(this)
    }

    private fun initializeList(view: View) {
        timerList = view.findViewById(R.id.list_timers)

        timerListSectionAdapter = TimerListSectionAdapterDelegate()
        timerList.layoutManager = LinearLayoutManager(activity)
        timerList.adapter = timerListSectionAdapter

        timerList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    /**
     * Setter used by {@link #fabAnimator}
     */
    @Keep
    @SuppressWarnings("unused")
    private fun setFabScale(scale: Float) {
        fab.scaleX = scale
        fab.scaleY = scale
    }

    companion object {
        const val TAG = "TimerFragment"

        private const val PROPERTY_FAB_ANIMATION = "fabScale"

        private const val FAB_ANIMATION_DURATION = 1000L
        private const val FAB_ANIMATION_DELAY = 400L
    }
}