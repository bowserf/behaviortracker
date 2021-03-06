package fr.bowser.behaviortracker.timerlist

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.*
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.google.android.gms.common.wrappers.InstantApps
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.alarm.AlarmTimerDialog
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import fr.bowser.behaviortracker.timer.Timer
import javax.inject.Inject

class TimerFragment : Fragment(), TimerContract.Screen {

    @Inject
    lateinit var presenter: TimerPresenter

    private lateinit var timerAdapter: TimerAdapter

    private lateinit var fab: FloatingActionButton

    private lateinit var emptyListView: ImageView

    private lateinit var emptyListText: TextView

    private lateinit var list: RecyclerView

    private var mSpanCount: Int = 1

    private val handler = Handler()

    private val runnable = Runnable {
        presenter.definitivelyRemoveTimer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        setupGraph()

        mSpanCount = resources.getInteger(R.integer.list_timers_number_spans)
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
            R.id.menu_settings -> {
                presenter.onClickSettings()
                return true
            }
            R.id.menu_alarm -> {
                presenter.onClickAlarm()
            }
            R.id.menu_rewards -> {
                presenter.onClickRewards()
            }
        }
        return false
    }

    override fun displayResetAllDialog() {
        val message = resources.getString(R.string.home_dialog_confirm_reset_all_timers)
        val builder = AlertDialog.Builder(context!!)
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
        alertDialog.show(fragmentManager!!, AlarmTimerDialog.TAG)
    }

    override fun displayRewardsView() {
        findNavController().navigate(R.id.rewards_screen)
    }

    override fun displayCreateTimerView() {
        CreateTimerDialog.showDialog(activity as AppCompatActivity, false)
    }

    override fun displayTimerList(timers: List<Timer>) {
        timerAdapter.setTimersList(timers)
    }

    override fun onTimerRemoved(timer: Timer) {
        timerAdapter.removeTimer(timer)
    }

    override fun onTimerAdded(timer: Timer) {
        timerAdapter.addTimer(timer)
    }

    override fun isTimerListEmpty(): Boolean {
        return timerAdapter.getTimerList().isEmpty()
    }

    override fun displayEmptyListView() {
        list.visibility = INVISIBLE
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
        list.visibility = VISIBLE
        emptyListView.visibility = INVISIBLE
        emptyListText.visibility = INVISIBLE
    }

    override fun displayCancelDeletionView() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, CANCEL_SUPPRESSION_DISPLAY_TIME.toLong())

        Snackbar.make(
                list,
                getString(R.string.timer_view_timer_has_been_removed),
                CANCEL_SUPPRESSION_DISPLAY_TIME
        )
                .setAction(android.R.string.cancel) {
                    handler.removeCallbacks(runnable)
                    presenter.cancelTimerDeletion()
                }
                .show()
    }

    private fun setupGraph() {
        val build = DaggerTimerComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .timerModule(TimerModule(this))
                .build()
        build.inject(this)
    }

    private fun initializeList(view: View) {
        list = view.findViewById(R.id.list_timers)

        list.layoutManager = GridLayoutManager(activity, mSpanCount, RecyclerView.VERTICAL, false)
        list.setHasFixedSize(true)
        timerAdapter = TimerAdapter()
        list.adapter = timerAdapter

        val swipeHandler = TimerListGesture(context!!, TimerListGestureListener())
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(list)

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        val margin = resources.getDimensionPixelOffset(R.dimen.default_space_1_5)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
            ) {
                var currentPosition = parent.getChildAdapterPosition(view)
                // When an item is removed, getChildAdapterPosition returns NO_POSITION but this
                // method is call at the animation start so position = -1 and we don't apply the
                // good top margin. By calling getChildLayoutPosition, we get the view position
                // and we fix the temporary animation issue.
                if (currentPosition == NO_POSITION) {
                    currentPosition = parent.getChildLayoutPosition(view)
                }
                if (currentPosition < mSpanCount) {
                    outRect.top = margin
                }

                outRect.left = margin
                outRect.right = margin
                outRect.bottom = margin
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

    inner class TimerListGestureListener : TimerListGesture.Listener {
        override fun onItemMove(fromPosition: Int, toPosition: Int) {
            timerAdapter.onItemMove(fromPosition, toPosition)
        }

        override fun onSelectedChangedUp() {
            val timerList = timerAdapter.getTimerList()
            presenter.onReorderFinished(timerList)
        }

        override fun onSwiped(position: Int) {
            val timer = timerAdapter.getTimer(position)
            presenter.onTimerSwiped(timer)
        }
    }

    companion object {
        const val TAG = "TimerFragment"

        private const val PROPERTY_FAB_ANIMATION = "fabScale"

        private const val FAB_ANIMATION_DURATION = 1000L
        private const val FAB_ANIMATION_DELAY = 400L

        private const val CANCEL_SUPPRESSION_DISPLAY_TIME = 3000
    }
}