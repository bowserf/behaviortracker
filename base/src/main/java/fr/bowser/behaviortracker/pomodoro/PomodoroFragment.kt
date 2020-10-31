package fr.bowser.behaviortracker.pomodoro

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.choosepomodorotimer.ChoosePomodoroTimerDialog
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject

class PomodoroFragment : Fragment(R.layout.fragment_pomodoro), PomodoroContract.Screen {

    @Inject
    lateinit var presenter: PomodoroPresenter

    private lateinit var emptyContent: View
    private lateinit var startSession: FloatingActionButton

    private lateinit var pomodoroSessionContent: View
    private lateinit var currentTimeTv: TextView
    private lateinit var activeTimerTv: TextView
    private lateinit var progresssBar: ProgressBar
    private lateinit var playPauseButton: View
    private lateinit var playPauseIcon: ImageView
    private lateinit var playPauseTitle: TextView
    private lateinit var stopButton: View
    private lateinit var doNotDisturb: View
    private lateinit var doNotDisturbIcon: ImageView
    private lateinit var doNotDisturbText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyContent = view.findViewById(R.id.pomodoro_content_empty)
        startSession = view.findViewById(R.id.pomodoro_choose_timer)

        pomodoroSessionContent = view.findViewById(R.id.pomodoro_content_session)
        progresssBar = view.findViewById(R.id.pomodoro_progress_bar)
        currentTimeTv = view.findViewById(R.id.pomodoro_current_time)
        activeTimerTv = view.findViewById(R.id.pomodoro_active_timer)
        playPauseButton = view.findViewById(R.id.pomodoro_play_pause)
        playPauseTitle = view.findViewById(R.id.pomodoro_play_pause_title)
        playPauseIcon = view.findViewById(R.id.pomodoro_play_pause_icon)
        stopButton = view.findViewById(R.id.pomodoro_stop)
        doNotDisturb = view.findViewById(R.id.pomodoro_toggle_dnd)
        doNotDisturbText = view.findViewById(R.id.pomodoro_toggle_dnd_text)
        doNotDisturbIcon = view.findViewById(R.id.pomodoro_toggle_dnd_icon)

        stopButton.setOnClickListener { presenter.onClickStopPomodoro() }
        playPauseButton.setOnClickListener { presenter.onClickChangePomodoroState() }
        startSession.setOnClickListener { presenter.onClickStartSession() }
        doNotDisturb.setOnClickListener { presenter.onClickDoNotDisturb() }

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)!!
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pomodoro, menu)

        if (presenter.isInstantApp()) {
            menu.removeItem(R.id.menu_settings)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                presenter.onClickSettings()
                return true
            }
        }
        return false
    }

    override fun updateTime(currentTime: Long) {
        currentTimeTv.text = TimeConverter.convertSecondsToHumanTime(currentTime, false)
        progresssBar.progress = currentTime.toInt()
    }

    override fun updatePomodoroTimer(timer: Timer, currentTime: Long, duration: Long) {
        emptyContent.visibility = View.GONE
        startSession.visibility = View.GONE

        pomodoroSessionContent.visibility = View.VISIBLE

        activeTimerTv.text = timer.name
        progresssBar.max = duration.toInt()

        updateTime(currentTime)

        val progressDrawable = progresssBar.progressDrawable
        if (progressDrawable is RotateDrawable) {
            progressDrawable.drawable!!.setColorFilter(
                ColorUtils.getColor(context!!, timer.color),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    override fun displayChoosePomodoroTimer() {
        ChoosePomodoroTimerDialog.showDialog(activity as AppCompatActivity)
    }

    override fun displayEmptyView() {
        emptyContent.visibility = View.VISIBLE
        startSession.visibility = View.VISIBLE

        pomodoroSessionContent.visibility = View.GONE

        activity!!.invalidateOptionsMenu()

        startFabAnimation()
    }

    override fun displayPomodoroDialog() {
        val fragmentManager = activity!!.supportFragmentManager
        if (fragmentManager.findFragmentByTag(PomodoroSessionDialog.TAG) != null) {
            return
        }
        val pomodoroDialogSession = PomodoroSessionDialog.newInstance()
        pomodoroDialogSession.show(fragmentManager, PomodoroSessionDialog.TAG)
    }

    override fun displayNoTimerAvailable() {
        Snackbar.make(
            emptyContent,
            getString(R.string.pomodoro_no_timer_available),
            Snackbar.LENGTH_SHORT
        )
            .setAction(context!!.resources.getString(R.string.pomodoro_create_timer)) {
                presenter.onClickCreateTimer()
            }
            .show()
    }

    override fun displayCreateTimerScreen() {
        CreateTimerDialog.showDialog(activity as AppCompatActivity, true)
    }

    override fun hidePomodoroDialog() {
        val fragmentManager = activity!!.supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(PomodoroSessionDialog.TAG)
        if (fragment != null) {
            val transaction = fragmentManager.beginTransaction()
            transaction.remove(fragment)
            transaction.commit()
        }
    }

    override fun displaySettings() {
        findNavController().navigate(R.id.settings_screen)
    }

    override fun displayPomodoroState(isRunning: Boolean) {
        if (isRunning) {
            playPauseIcon.setImageResource(R.drawable.ic_pause)
            playPauseTitle.text = resources.getString(R.string.pomodoro_pause)
        } else {
            playPauseIcon.setImageResource(R.drawable.ic_play)
            playPauseTitle.text = resources.getString(R.string.pomodoro_play)
        }
    }

    override fun hideDoNotDisturb() {
        doNotDisturb.visibility = View.GONE
    }

    override fun enableDoNotDisturb(enable: Boolean) {
        val color: Int
        if (enable) {
            doNotDisturb.setBackgroundResource(R.drawable.pomodoro_do_not_disturb_bg_selected)
            color = ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            doNotDisturb.background = null
            color = ContextCompat.getColor(requireContext(), R.color.icon_day_night)
        }
        doNotDisturbText.setTextColor(color)
        doNotDisturbIcon.imageTintList = ColorStateList.valueOf(color)
    }

    private fun startFabAnimation() {
        val fabAnimator = ObjectAnimator.ofFloat(
            this,
            PROPERTY_FAB_ANIMATION,
            1f, 1.15f, 1f
        )
        fabAnimator.duration = FAB_ANIMATION_DURATION
        fabAnimator.repeatCount = 1
        fabAnimator.interpolator = AccelerateDecelerateInterpolator()
        fabAnimator.startDelay = FAB_ANIMATION_DELAY
        fabAnimator.start()
    }

    /**
     * Setter used by {@link #fabAnimator}
     */
    @Keep
    @SuppressWarnings("unused")
    private fun setFabScale(scale: Float) {
        startSession.scaleX = scale
        startSession.scaleY = scale
    }

    private fun setupGraph() {
        val build = DaggerPomodoroComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
            .pomodoroModule(PomodoroModule(this))
            .build()
        build.inject(this)
    }

    companion object {
        const val TAG = "PomodoroFragment"

        private const val PROPERTY_FAB_ANIMATION = "fabScale"

        private const val FAB_ANIMATION_DURATION = 1000L
        private const val FAB_ANIMATION_DELAY = 400L
    }
}