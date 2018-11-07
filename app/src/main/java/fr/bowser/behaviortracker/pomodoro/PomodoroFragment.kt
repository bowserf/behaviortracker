package fr.bowser.behaviortracker.pomodoro

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.choosepomodorotimer.ChoosePomodoroTimerDialog
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import fr.bowser.behaviortracker.setting.SettingActivity
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject


class PomodoroFragment : Fragment(), PomodoroContract.Screen {

    @Inject
    lateinit var presenter: PomodoroPresenter

    private lateinit var emptyContent: View
    private lateinit var defaultImage: ImageView
    private lateinit var description: TextView

    private lateinit var pomodoroSessionContent: View
    private lateinit var currentTimeTv: TextView
    private lateinit var activeTimerTv: TextView
    private lateinit var progresssBar: ProgressBar

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyContent = view.findViewById(R.id.pomodoro_content_empty)
        defaultImage = view.findViewById(R.id.pomodoro_default_image)
        description = view.findViewById(R.id.pomodoro_description)

        pomodoroSessionContent = view.findViewById(R.id.pomodoro_content_session)
        progresssBar = view.findViewById(R.id.pomodoro_progress_bar)
        currentTimeTv = view.findViewById(R.id.pomodoro_current_time)
        activeTimerTv = view.findViewById(R.id.pomodoro_active_timer)

        fab = view.findViewById(R.id.pomodoro_choose_timer)

        fab.setOnClickListener { presenter.onClickFab() }
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
        menu.clear()
        inflater.inflate(R.menu.menu_pomodoro, menu)

        val stopPomodoro = menu.findItem(R.id.menu_stop_pomodoro)
        stopPomodoro.isVisible = presenter.isRunning()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_stop_pomodoro -> {
                presenter.onClickStopPomodoro()
                return true
            }
            R.id.menu_settings -> {
                SettingActivity.startActivity(context!!)
            }
        }

        return false
    }

    override fun updateTime(timer: Timer, currentTime: Long) {
        currentTimeTv.text = TimeConverter.convertSecondsToHumanTime(currentTime, false)
        progresssBar.progress = currentTime.toInt()
    }

    override fun updatePomodoroTimer(timer: Timer, currentTime: Long, duration: Long) {
        emptyContent.visibility = View.GONE
        pomodoroSessionContent.visibility = View.VISIBLE

        activeTimerTv.text = timer.name
        currentTimeTv.text = TimeConverter.convertSecondsToHumanTime(currentTime, false)

        progresssBar.max = duration.toInt()
        progresssBar.progress = currentTime.toInt()

        val progressDrawable = progresssBar.progressDrawable
        if (progressDrawable is RotateDrawable) {
            progressDrawable.drawable!!.setColorFilter(
                    ColorUtils.getColor(context!!, timer.color),
                    PorterDuff.Mode.SRC_ATOP)
        }
        val backgroundDrawable = progresssBar.background
        if (backgroundDrawable is GradientDrawable) {
            val rgb = ColorUtils.getColor(context!!, timer.color)
            backgroundDrawable.setColorFilter(
                    Color.argb(PROGRESS_BAR_BACKGROUND_ALPHA,
                            Color.red(rgb),
                            Color.green(rgb),
                            Color.blue(rgb)),
                    PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun displayChoosePomodoroTimer() {
        ChoosePomodoroTimerDialog.showDialog(activity as AppCompatActivity)
    }

    override fun displayEmptyView() {
        emptyContent.visibility = View.VISIBLE
        pomodoroSessionContent.visibility = View.GONE

        activity!!.invalidateOptionsMenu()
    }

    override fun displayPauseIcon() {
        fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.pomodoro_pause_session))
        fab.setImageDrawable(context!!.getDrawable(R.drawable.ic_pause_white))

        activity!!.invalidateOptionsMenu()
    }

    override fun displayPlayIcon() {
        fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.pomodoro_resume_session))
        fab.setImageDrawable(context!!.getDrawable(R.drawable.ic_play_white))
    }

    override fun displayStartIcon() {
        fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.pomodoro_start_session))
        fab.setImageDrawable(context!!.getDrawable(R.drawable.ic_flag))
    }

    override fun displayPomodoroDialog() {
        val fragmentManager = activity!!.supportFragmentManager
        val pomodoroDialogSession = PomodoroSessionDialog.newInstance()
        pomodoroDialogSession.show(fragmentManager, PomodoroSessionDialog.TAG)
    }

    override fun displayNoTimerAvailable() {
        Snackbar.make(
                emptyContent,
                getString(R.string.pomodoro_no_timer_available),
                Snackbar.LENGTH_SHORT)
                .setAction(context!!.resources.getString(R.string.pomodoro_create_timer)) {
                    presenter.onClickCreateTimer()
                }
                .show()
    }

    override fun displayCreateTimer() {
        CreateTimerDialog.showDialog(activity as AppCompatActivity, true)
    }

    override fun dismissPomodoroDialog() {
        val fragmentManager = activity!!.supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(PomodoroSessionDialog.TAG)
        if(fragment != null){
            val transaction = fragmentManager.beginTransaction()
            transaction.remove(fragment)
            transaction.commit()
        }
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

        const val PROGRESS_BAR_BACKGROUND_ALPHA = 100
    }

}