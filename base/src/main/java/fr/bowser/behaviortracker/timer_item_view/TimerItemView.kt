package fr.bowser.behaviortracker.timer_item_view

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.ScaleAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    private val imm by lazy {
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val containerRunning: View by bind(R.id.timer_item_view_container_running)
    private val timerTimeTv: TextView by bind(R.id.timer_item_view_timer_chrono)
    private val lastUpdateTimestamp: TextView by bind(
        R.id.timer_item_view_timer_last_update_timestamp,
    )
    private val timerName: TextView by bind(R.id.timer_item_view_timer_name)
    private val menu: ImageView by bind(R.id.timer_item_view_menu)
    private val color: View by bind(R.id.timer_item_view_color)
    private val btnPlayPause: ImageView by bind(R.id.timer_item_view_play_pause)

    private val containerFinished: View by bind(R.id.timer_item_view_container_finished)
    private val timerNameFinished: TextView by bind(R.id.timer_item_view_container_finished_timer_name)
    private val timerTimeFinishedTv: TextView by bind(R.id.timer_item_view_container_finished_chrono)

    init {
        setupGraph()

        inflate(context, R.layout.timer_item_view, this)

        // card radius
        radius = resources.getDimension(R.dimen.default_space_half)

        color.setOnClickListener { presenter.onClickCard() }
        menu.setOnClickListener { displayMenu() }
        findViewById<View>(R.id.timer_item_view_time_update).setOnClickListener {
            presenter.onClickUpdateTimer()
        }
        findViewById<View>(R.id.timer_item_view_container_finished_restart).setOnClickListener {
            presenter.onClickRestartTimer()
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

                R.id.timer_item_view_finish -> {
                    presenter.onClickFinishTimer()
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
            val timeStr = TimeConverter.convertSecondsToHumanTime(time)
            timerTimeTv.text = timeStr
            timerTimeFinishedTv.text = timeStr
        }

        override fun setName(name: String) {
            timerName.text = name
            timerNameFinished.text = name
        }

        override fun displayRenameDialog(oldName: String) {
            val alertDialog = MaterialAlertDialogBuilder(context)
            alertDialog.setMessage(resources.getString(R.string.timer_row_rename))

            val rootView =
                LayoutInflater.from(context).inflate(R.layout.timer_item_view_rename_timer, null)
            val newTimerNameEditText =
                rootView.findViewById<EditText>(R.id.timer_item_view_rename_timer_edit_text)
            newTimerNameEditText.setMultiLineCapSentencesAndDoneAction()

            val defaultInterruptName = resources.getString(R.string.timer_interrupt_name, "")
            if (oldName.contains(defaultInterruptName)) {
                newTimerNameEditText.setHint(oldName)
            } else {
                newTimerNameEditText.setText(oldName)
                newTimerNameEditText.setSelection(newTimerNameEditText.text.length)
            }

            alertDialog.setView(rootView)

            alertDialog.setPositiveButton(android.R.string.ok) { _, _ ->
                val newName = newTimerNameEditText.text.toString()
                if (newName.isNotBlank()) {
                    presenter.onTimerNameUpdated(newName)
                }
            }

            alertDialog.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

            val dialog = alertDialog.create()
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.show()

            newTimerNameEditText.setOnEditorActionListener { view, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val newName = newTimerNameEditText.text.toString()
                    if (newName.isNotBlank()) {
                        presenter.onTimerNameUpdated(newName)
                        dialog.dismiss()
                    }
                    return@setOnEditorActionListener true
                }
                false
            }

            newTimerNameEditText.requestFocus()

            displayKeyboard(newTimerNameEditText)
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

        override fun displayConfirmResetTimer(timer: Timer) {
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.home_dialog_confirm_reset_timer))
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    presenter.onConfirmResetTimer(timer)
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                .show()
        }

        override fun setModeFinished(isFinished: Boolean) {
            if (isFinished) {
                containerRunning.visibility = View.GONE
                containerFinished.visibility = View.VISIBLE
                setOnClickListener(null)
            } else {
                containerRunning.visibility = View.VISIBLE
                containerFinished.visibility = View.GONE
                setOnClickListener { manageClickPlayPauseButton() }
            }
        }
    }

    private fun displayKeyboard(editText: EditText) {
        Handler().postDelayed(
            { imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT) },
            200,
        )
    }

    companion object {
        private const val SCALE_VALUE_ANIMATION = 1.05f
        private const val ANIMATION_DURATION = 400L
    }
}
