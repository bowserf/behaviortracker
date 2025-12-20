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
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.alarm_view.AlarmViewDialog
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.create_timer_view.CreateTimerViewBottomSheetFragment
import fr.bowser.behaviortracker.explain_permission_request_view.ExplainPermissionRequestViewModel
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.applyStatusBarPadding
import fr.bowser.feature_review.ReviewActivityContainer
import javax.inject.Inject

class TimerListFragment : Fragment(fr.bowser.behaviortracker.R.layout.timer_list_view) {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViewByIds(view)

        initializeList()

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
        inflater.inflate(fr.bowser.behaviortracker.R.menu.timer_list_menu, menu)

        val reviewMenuItem = menu.findItem(fr.bowser.behaviortracker.R.id.timer_list_menu_review)
        reviewMenuItem.isVisible = !presenter.isReviewAlreadyDone()

        val showEndedTimersItem =
            menu.findItem(fr.bowser.behaviortracker.R.id.timer_list_menu_show_ended_timers)
        val showEndedTimersSwitch = showEndedTimersItem.actionView as SwitchCompat
        showEndedTimersSwitch.isChecked = presenter.shouldDisplayEndedTasks()
        showEndedTimersSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            presenter.onChangeStateShowEndedTimer(isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            fr.bowser.behaviortracker.R.id.timer_list_menu_review -> {
                presenter.onClickRateApp(createReviewActivityContainer())
                return true
            }

            fr.bowser.behaviortracker.R.id.timer_list_menu_export_timers -> {
                presenter.onClickExportTimers()
                return true
            }

            fr.bowser.behaviortracker.R.id.timer_list_menu_reset_all -> {
                presenter.onClickResetAll()
                return true
            }

            fr.bowser.behaviortracker.R.id.timer_list_menu_remove_all -> {
                presenter.onClickRemoveAllTimers()
                return true
            }

            fr.bowser.behaviortracker.R.id.timer_list_menu_settings -> {
                presenter.onClickSettings()
                return true
            }

            fr.bowser.behaviortracker.R.id.timer_list_menu_alarm -> {
                presenter.onClickAlarm()
                return true
            }

            fr.bowser.behaviortracker.R.id.timer_list_menu_rewards -> {
                presenter.onClickRewards()
                return true
            }
        }
        return false
    }

    private fun createScreen() = object : TimerListViewContract.Screen {
        override fun displayResetAllDialog() {
            val message =
                resources.getString(fr.bowser.behaviortracker.R.string.home_dialog_confirm_reset_all_timers)
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
            findNavController().navigate(fr.bowser.behaviortracker.R.id.settings_screen)
        }

        override fun displayAlarmTimerDialog() {
            val alertDialog = AlarmViewDialog.newInstance()
            alertDialog.show(childFragmentManager, AlarmViewDialog.TAG)
        }

        override fun displayRewardsView() {
            findNavController().navigate(fr.bowser.behaviortracker.R.id.rewards_screen)
        }

        override fun displayRemoveAllTimersConfirmationDialog() {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(fr.bowser.behaviortracker.R.string.timer_list_remove_all_timers_title))
                .setMessage(resources.getString(fr.bowser.behaviortracker.R.string.timer_list_remove_all_timers_message))
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
            timerAdapter.populate(timers, true)
        }

        override fun displayEmptyListView() {
            timerList.visibility = INVISIBLE
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
            timerList.visibility = VISIBLE
            emptyListView.visibility = INVISIBLE
            emptyListText.visibility = INVISIBLE
        }

        override fun updateTotalTime(totalTime: Long) {
            timerAdapter.updateTotalTime(totalTime)
        }

        override fun displayExportSucceeded() {
            Toast.makeText(
                requireContext(),
                fr.bowser.behaviortracker.R.string.timer_list_export_succeeded,
                Toast.LENGTH_SHORT,
            ).show()
        }

        override fun scrollToTimer(timerId: Long) {
            val timerIndex = timerAdapter.getTimerList().indexOfFirst { timerId == it.id }
            timerList.smoothScrollToPosition(timerIndex)
        }

        override fun reorderTimer(
            timers: List<Timer>,
            fromPosition: Int,
            toPosition: Int,
        ) {
            timerAdapter.populate(timers, false)
            timerAdapter.reorderTimer(fromPosition, toPosition)
        }

        override fun removeTimer(
            timers: List<Timer>,
            position: Int,
        ) {
            timerAdapter.populate(timers, false)
            timerAdapter.removeTimer(position)
        }

        override fun invalidateMenu() {
            requireActivity().invalidateOptionsMenu()
        }

        override fun displayAskScheduleAlarmPermission() {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(fr.bowser.behaviortracker.R.string.timer_list_schedule_alarm_permission_title)
                .setMessage(fr.bowser.behaviortracker.R.string.timer_list_schedule_alarm_permission_message)
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    // nothing to do
                }
                .setPositiveButton(fr.bowser.behaviortracker.R.string.timer_list_schedule_alarm_permission_positive_button) { _, _ ->
                    presenter.onClickAskScheduleAlarmSettings()
                }
                .show()
        }

        override fun displayAskNotificationDisplay() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                throw IllegalStateException("You can't call this method on API < 33")
            }
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(fr.bowser.behaviortracker.R.string.timer_list_ask_notification_display_title)
                .setMessage(fr.bowser.behaviortracker.R.string.timer_list_ask_notification_display_message)
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    // nothing to do
                }
                .setPositiveButton(fr.bowser.behaviortracker.R.string.timer_list_ask_notification_display_positive_button) { _, _ ->
                    presenter.onClickAskNotificationDisplaySettings()
                }
                .show()
        }

        override fun displayAskNotificationPermissionForManagingTimers() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                throw IllegalStateException("You can't call this method on API < 33")
            }
            MaterialAlertDialogBuilder(
                requireContext(),
                fr.bowser.behaviortracker.R.style.AlertDialogTheme,
            )
                .setTitle(fr.bowser.behaviortracker.R.string.timer_list_notification_permission_title)
                .setMessage(fr.bowser.behaviortracker.R.string.timer_list_notification_permission_description)
                .setPositiveButton(fr.bowser.behaviortracker.R.string.timer_list_notification_permission_positive) { _, _ ->
                    timerNotificationActivityResultLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS,
                    )
                }
                .setCancelable(false)
                .show()
        }

        override fun showInterruptTimer(show: Boolean) {
            if (show) {
                interruptTimer.show()
            } else {
                interruptTimer.hide()
            }
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
                timerList,
                resources.getString(fr.bowser.behaviortracker.R.string.timer_view_timer_has_been_removed),
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

        val margin =
            resources.getDimensionPixelOffset(fr.bowser.behaviortracker.R.dimen.default_space_1_5)
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
        val toolbar =
            view.findViewById<Toolbar>(fr.bowser.behaviortracker.R.id.timer_list_view_toolbar)!!
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
            val timer = timerAdapter.getTimerList()[position]
            presenter.onTimerSwiped(timer.id)
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
        fab = view.findViewById(fr.bowser.behaviortracker.R.id.timer_list_view_add_timer)
        interruptTimer =
            view.findViewById(fr.bowser.behaviortracker.R.id.timer_list_view_start_interrupt_timer)
        emptyListView =
            view.findViewById(fr.bowser.behaviortracker.R.id.timer_list_view_empty_list_view)
        emptyListText =
            view.findViewById(fr.bowser.behaviortracker.R.id.timer_list_view_empty_list_text)
        timerList = view.findViewById(fr.bowser.behaviortracker.R.id.timer_list_view_list_timers)
    }

    companion object {
        const val TAG = "TimerFragment"

        private const val PROPERTY_FAB_ANIMATION = "fabScale"

        private const val FAB_ANIMATION_DURATION = 1000L
        private const val FAB_ANIMATION_DELAY = 400L
    }
}
