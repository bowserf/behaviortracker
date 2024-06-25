package fr.bowser.behaviortracker.timer_item_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.ScaleAnimation
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_list_view.TimerListFragmentDirections
import fr.bowser.behaviortracker.update_timer_time_view.UpdateTimerTimeDialog
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import fr.bowser.behaviortracker.utils.ViewExtension.bind
import fr.bowser.behaviortracker.utils.setMultiLineCapSentencesAndDoneAction
import javax.inject.Inject


class TimerItemView(context: Context) : CardView(context) {

    @Inject
    lateinit var presenter: TimerItemViewContract.Presenter

    private val screen = createScreen()

    private val chrono: TextView by bind(R.id.timer_item_view_timer_chrono)
    private val lastUpdateTimestamp: TextView by bind(
        R.id.timer_item_view_timer_last_update_timestamp,
    )
    private val tvName: TextView by bind(R.id.timer_item_view_timer_name)
    private val menu: ImageView by bind(R.id.timer_item_view_menu)
    private val color: View by bind(R.id.timer_item_view_color)
    private val btnPlayPause: ImageView by bind(R.id.timer_item_view_play_pause)

    init {
        setupGraph()

        inflate(context, R.layout.timer_item_view, this)

        // card radius
        radius = resources.getDimension(R.dimen.default_space_half)

        setOnClickListener { manageClickPlayPauseButton() }

        color.setOnClickListener { presenter.onClickCard() }
        menu.setOnClickListener { displayMenu() }
        findViewById<View>(R.id.timer_item_view_time_update).setOnClickListener {
            presenter.onClickUpdateTimer()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onStart()
    }

    override fun onDetachedFromWindow() {
        presenter.onStop()
        super.onDetachedFromWindow()
    }

    fun setTimer(timer: Timer) {
        presenter.setTimer(timer)
    }

    private fun displayMenu() {
        val popup = PopupMenu(context, menu)

        popup.menuInflater.inflate(R.menu.timer_item_view_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.timer_item_view_reset -> {
                    presenter.onClickResetTimer()
                }

                R.id.timer_item_view_rename -> {
                    presenter.onClickRenameTimer()
                }

                R.id.timer_item_view_start_pomodoro -> {
                    presenter.onClickStartPomodoro()
                }
            }
            true
        }

        popup.show()
    }

    private fun manageClickPlayPauseButton() {
        presenter.timerStateChange()
    }

    private fun updateBtnPlayPause(isActivate: Boolean) {
        if (isActivate) {
            btnPlayPause.setImageResource(R.drawable.timer_item_view_pause)
        } else {
            btnPlayPause.setImageResource(R.drawable.timer_item_view_play)
        }
    }

    private fun playAnimation() {
        val anim = ScaleAnimation(
            1f,
            SCALE_VALUE_ANIMATION,
            1f,
            SCALE_VALUE_ANIMATION,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
        )
        anim.duration = ANIMATION_DURATION
        anim.setAnimationListener(
            object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // nothing to do
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val animReverse = ScaleAnimation(
                        SCALE_VALUE_ANIMATION,
                        1f,
                        SCALE_VALUE_ANIMATION,
                        1f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                    )
                    animReverse.duration = ANIMATION_DURATION
                    startAnimation(animReverse)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // nothing to do
                }
            },
        )
        startAnimation(anim)
    }

    private fun setupGraph() {
        val component = DaggerTimerItemViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .timerItemViewModule(TimerItemViewModule(screen))
            .build()
        component.inject(this)
    }

    private fun createScreen() = object : TimerItemViewContract.Screen {

        override fun setTime(time: Long) {
            chrono.text = TimeConverter.convertSecondsToHumanTime(time)
        }

        override fun setName(name: String) {
            tvName.text = name
        }

        override fun displayRenameDialog(oldName: String) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage(resources.getString(R.string.timer_row_rename))

            val rootView =
                LayoutInflater.from(context).inflate(R.layout.timer_item_view_rename_timer, null)
            val newTimerNameEditText =
                rootView.findViewById<EditText>(R.id.timer_item_view_rename_timer_edit_text)
            newTimerNameEditText.setMultiLineCapSentencesAndDoneAction()
            newTimerNameEditText.setText(oldName)
            newTimerNameEditText.setSelection(newTimerNameEditText.text.length)

            alertDialog.setView(rootView)

            alertDialog.setPositiveButton(android.R.string.ok) { _, _ ->
                val newName = newTimerNameEditText.text.toString()
                presenter.onTimerNameUpdated(newName)
            }

            alertDialog.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

            val dialog = alertDialog.create()
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.show()

            newTimerNameEditText.setOnEditorActionListener { view, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val newName = newTimerNameEditText.text.toString()
                    presenter.onTimerNameUpdated(newName)
                    dialog.dismiss()
                    return@setOnEditorActionListener true
                }
                false
            }
        }

        override fun statusUpdated(activate: Boolean) {
            updateBtnPlayPause(activate)
        }

        override fun startShowMode(id: Long) {
            val action = TimerListFragmentDirections.actionTimerListScreenToShowModeScreen(id)
            findNavController().navigate(action)
        }

        override fun displayUpdateTimerTimeDialog(timerId: Long) {
            val updateTimerTimeDialog = UpdateTimerTimeDialog.newInstance(timerId)
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            updateTimerTimeDialog.show(fragmentManager, UpdateTimerTimeDialog.TAG)
        }

        override fun updateLastUpdatedDate(date: String) {
            lastUpdateTimestamp.text = date
        }

        override fun playSelectedAnimation() {
            playAnimation()
        }

        override fun setColorId(colorId: Int) {
            color.setBackgroundColor(ColorUtils.getColor(context!!, colorId))
        }
    }

    companion object {
        private const val SCALE_VALUE_ANIMATION = 1.05f
        private const val ANIMATION_DURATION = 400L
    }
}
