package fr.bowser.behaviortracker.timer_list_view

import android.Manifest
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.alarm_view.AlarmViewDialog
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.create_timer_view.CreateTimerViewBottomSheetFragment
import fr.bowser.behaviortracker.explain_permission_request_view.ExplainPermissionRequestViewModel
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.TimeConverter
import fr.bowser.behaviortracker.utils.applyStatusBarPadding
import fr.bowser.feature_review.ReviewActivityContainer
import javax.inject.Inject

class TimerListFragment : Fragment(R.layout.timer_list_view) {

    @Inject
    lateinit var presenter: TimerListViewContract.Presenter

    private val screen = createScreen()

    private val alarmNotificationActivityResultLauncher =
        createAlarmNotificationActivityResultLauncher()

    private val timerNotificationActivityResultLauncher =
        createTimerNotificationActivityResultLauncher()

    private val timerAdapter = TimerListViewAdapter()

    private lateinit var fab: FloatingActionButton
    private lateinit var interruptTimer: FloatingActionButton
    private lateinit var emptyListView: ImageView
    private lateinit var emptyListText: TextView
    private lateinit var timerList: RecyclerView
    private lateinit var totalTimeTv: TextView
    private lateinit var timerListContainer: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViewByIds(view)

        initializeList()

        timerListContainer.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY) {
                    fab.hide()
                } else {
                    fab.show()
                }
            },
        )

        fab.setOnClickListener { presenter.onClickAddTimer() }
        interruptTimer.setOnClickListener { presenter.onClickInterruptTimer() }

        initializeToolbar(view)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_activity_menu, menu)
        val reviewMenuItem = menu.findItem(R.id.home_activity_menu_review)
        reviewMenuItem.isVisible = !presenter.isReviewAlreadyDone()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_activity_menu_review -> {
                presenter.onClickRateApp(createReviewActivityContainer())
                return true
            }

            R.id.home_activity_menu_export_timers -> {
                presenter.onClickExportTimers()
                return true
            }

            R.id.home_activity_menu_reset_all -> {
                presenter.onClickResetAll()
                return true
            }

            R.id.home_activity_menu_remove_all -> {
                presenter.onClickRemoveAllTimers()
                return true
            }

            R.id.home_activity_menu_settings -> {
                presenter.onClickSettings()
                return true
            }

            R.id.home_activity_menu_alarm -> {
                presenter.onClickAlarm()
                return true
            }

            R.id.home_activity_menu_rewards -> {
                presenter.onClickRewards()
                return true
            }
        }
        return false
    }

    private fun createScreen() = object : TimerListViewContract.Screen {
        override fun displayResetAllDialog() {
            val message = resources.getString(R.string.home_dialog_confirm_reset_all_timers)
            val builder = MaterialAlertDialogBuilder(requireContext())
            builder.setMessage(message)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    presenter.onClickResetAllTimers()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    // do nothing
                }
                .show()
        }

        override fun displaySettingsView() {
            findNavController().navigate(R.id.settings_screen)
        }

        override fun displayAlarmTimerDialog() {
            val alertDialog = AlarmViewDialog.newInstance()
            alertDialog.show(childFragmentManager, AlarmViewDialog.TAG)
        }

        override fun displayRewardsView() {
            findNavController().navigate(R.id.rewards_screen)
        }

        override fun displayRemoveAllTimersConfirmationDialog() {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.timer_list_remove_all_timers_title))
                .setMessage(resources.getString(R.string.timer_list_remove_all_timers_message))
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    presenter.onClickConfirmRemoveAllTimers()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        override fun displayCreateTimerView() {
            CreateTimerViewBottomSheetFragment.showDialog(activity as AppCompatActivity, false)
        }

        override fun displayTimers(timers: List<Timer>) {
            timerAdapter.populate(timers)
        }

        override fun displayEmptyListView() {
            timerListContainer.visibility = INVISIBLE
            emptyListView.visibility = VISIBLE
            emptyListText.visibility = VISIBLE

            val fabAnimator = ObjectAnimator.ofFloat(
                this@TimerListFragment,
                PROPERTY_FAB_ANIMATION,
                1f,
                1.15f,
                1f,
            )
            fabAnimator.duration = FAB_ANIMATION_DURATION
            fabAnimator.repeatCount = 1
            fabAnimator.interpolator = AccelerateDecelerateInterpolator()
            fabAnimator.startDelay = FAB_ANIMATION_DELAY
            fabAnimator.start()
        }

        override fun displayListView() {
            timerListContainer.visibility = VISIBLE
            emptyListView.visibility = INVISIBLE
            emptyListText.visibility = INVISIBLE
        }

        override fun updateTotalTime(totalTime: Long) {
            val totalTimeStr = TimeConverter.convertSecondsToHumanTime(totalTime)
            totalTimeTv.text = resources.getString(R.string.timer_list_total_time, totalTimeStr)
        }

        override fun displayExportSucceeded() {
            Toast.makeText(
                requireContext(),
                R.string.timer_list_export_succeeded,
                Toast.LENGTH_SHORT,
            ).show()
        }

        override fun scrollToTimer(timerIndex: Int) {
            val y = timerList.getChildAt(timerIndex).y.toInt()
            timerListContainer.smoothScrollTo(0, y)
        }

        override fun reorderTimer(fromPosition: Int, toPosition: Int) {
            timerAdapter.reorderTimer(fromPosition, toPosition)
        }

        override fun invalidateMenu() {
            requireActivity().invalidateOptionsMenu()
        }

        override fun displayAskScheduleAlarmPermission() {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.timer_list_schedule_alarm_permission_title)
                .setMessage(R.string.timer_list_schedule_alarm_permission_message)
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    // nothing to do
                }
                .setPositiveButton(R.string.timer_list_schedule_alarm_permission_positive_button) { _, _ ->
                    presenter.onClickAskScheduleAlarmSettings()
                }
                .show()
        }

        override fun displayAskNotificationDisplay() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                throw IllegalStateException("You can't call this method on API < 33")
            }
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.timer_list_ask_notification_display_title)
                .setMessage(R.string.timer_list_ask_notification_display_message)
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    // nothing to do
                }
                .setPositiveButton(R.string.timer_list_ask_notification_display_positive_button) { _, _ ->
                    presenter.onClickAskNotificationDisplaySettings()
                }
                .show()
        }

        override fun displayAskNotificationPermissionForManagingTimers() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                throw IllegalStateException("You can't call this method on API < 33")
            }
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.timer_list_notification_permission_title)
                .setMessage(R.string.timer_list_notification_permission_description)
                .setPositiveButton(R.string.timer_list_notification_permission_positive) { _, _ ->
                    timerNotificationActivityResultLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS,
                    )
                }
                .setCancelable(false)
                .show()
        }

        override fun shouldShowNotificationPermissionRationale(): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                false
            }
        }

        override fun checkNotificationPermissionForAlarm() {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED -> {
                    presenter.onNotificationPermissionAlreadyGrantedForAlarm()
                }

                shouldShowNotificationPermissionRationale() -> {
                    presenter.shouldShowNotificationRequestPermissionRationaleForAlarm()
                }

                else -> {
                    // TODO
                    alarmNotificationActivityResultLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS,
                    )
                }
            }
        }

        override fun displayExplainNotificationPermissionForAlarm(
            explainPermissionRequestModel: ExplainPermissionRequestViewModel,
        ) {
            val action =
                TimerListFragmentDirections.actionTimerListScreenToExplainPermissionRequest(
                    explainPermissionRequestModel,
                )
            findNavController().navigate(action)
        }

        override fun displayCancelDeletionView(cancelDuration: Int) {
            Snackbar.make(
                timerListContainer,
                resources.getString(R.string.timer_view_timer_has_been_removed),
                cancelDuration,
            ).setAction(android.R.string.cancel) {
                presenter.onClickCancelTimerDeletion()
            }.show()
        }
    }

    private fun createReviewActivityContainer() = object : ReviewActivityContainer {
        override fun isActivityAccessible(): Boolean {
            return !activity!!.isDestroyed
        }

        override fun getActivity(): Activity {
            return activity!!
        }
    }

    private fun createAlarmNotificationActivityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { success ->
            if (success) {
                presenter.onNotificationPermissionGranted()
            } else {
                presenter.onNotificationPermissionDeclined()
            }
        }

    private fun createTimerNotificationActivityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
            // nothing to do
        }

    private fun setupGraph() {
        val build = DaggerTimerListViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .timerListViewModule(TimerListViewModule(screen))
            .build()
        build.inject(this)
    }

    private fun initializeList() {
        timerList.layoutManager = LinearLayoutManager(context)
        timerList.adapter = timerAdapter

        val swipeHandler = TimerListViewGesture(requireContext(), TimerListGestureListener())
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(timerList)

        val margin = resources.getDimensionPixelOffset(R.dimen.default_space_1_5)
        timerList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                var currentPosition = parent.getChildAdapterPosition(view)
                // When an item is removed, getChildAdapterPosition returns NO_POSITION but this
                // method is call at the animation start so position = -1 and we don't apply the
                // good top margin. By calling getChildLayoutPosition, we get the view position
                // and we fix the temporary animation issue.
                if (currentPosition == RecyclerView.NO_POSITION) {
                    currentPosition = parent.getChildLayoutPosition(view)
                }
                if (currentPosition < 1) {
                    outRect.top = margin
                }

                outRect.bottom = margin
            }
        })
    }

    private fun initializeToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.timer_list_view_toolbar)!!
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.applyStatusBarPadding()
    }

    inner class TimerListGestureListener : TimerListViewGesture.Listener {
        override fun onItemMove(fromPosition: Int, toPosition: Int) {
            presenter.onTimerPositionChanged(fromPosition, toPosition)
        }

        override fun onSelectedChangedUp() {
            // nothing to do
        }

        override fun onSwiped(position: Int) {
            presenter.onTimerSwiped(position)
        }
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

    private fun findViewByIds(view: View) {
        fab = view.findViewById(R.id.timer_list_view_add_timer)
        interruptTimer = view.findViewById(R.id.timer_list_view_start_interrupt_timer)
        emptyListView = view.findViewById(R.id.timer_list_view_empty_list_view)
        emptyListText = view.findViewById(R.id.timer_list_view_empty_list_text)
        timerList = view.findViewById(R.id.timer_list_view_list_timers)
        totalTimeTv = view.findViewById(R.id.timer_list_view_total_time)
        timerListContainer = view.findViewById(R.id.timer_list_view_container_list)
    }

    companion object {
        const val TAG = "TimerFragment"

        private const val PROPERTY_FAB_ANIMATION = "fabScale"

        private const val FAB_ANIMATION_DURATION = 1000L
        private const val FAB_ANIMATION_DELAY = 400L
    }
}
