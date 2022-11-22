package fr.bowser.behaviortracker.timer_item_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
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
import fr.bowser.behaviortracker.timer_list_view.TimerFragmentDirections
import fr.bowser.behaviortracker.update_timer_time_view.UpdateTimerTimeDialog
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject

class TimerRowView(context: Context) : CardView(context) {

    @Inject
    lateinit var presenter: TimerItemContract.Presenter

    private val screen = createScreen()

    private val chrono: TextView
    private val lastUpdateTimestamp: TextView
    private val tvName: TextView
    private val menu: ImageView
    private val color: View
    private val btnPlayPause: ImageView

    init {
        setupGraph()

        inflate(context, R.layout.item_timer, this)

        // card radius
        radius = resources.getDimension(R.dimen.default_space_half)

        setOnClickListener { manageClickPlayPauseButton() }

        btnPlayPause = findViewById(R.id.timer_play_pause)

        color = findViewById(R.id.timer_color)
        color.setOnClickListener { presenter.onClickCard() }
        chrono = findViewById(R.id.timer_chrono)
        lastUpdateTimestamp = findViewById(R.id.timer_last_update_timestamp)
        tvName = findViewById(R.id.timer_name)
        menu = findViewById(R.id.timer_menu)
        menu.setOnClickListener { displayMenu() }
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

        chrono.text = TimeConverter.convertSecondsToHumanTime(timer.time.toLong())
        tvName.text = timer.name
        color.setBackgroundColor(ColorUtils.getColor(context!!, timer.color))

        updateBtnPlayPause(timer.isActivate)
    }

    private fun displayMenu() {
        val popup = PopupMenu(context, menu)

        popup.menuInflater.inflate(R.menu.menu_item_timer, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_timer_update_timer_time -> {
                    presenter.onClickAddDuration()
                }
                R.id.item_timer_reset -> {
                    presenter.onClickResetTimer()
                }
                R.id.item_timer_delete -> {
                    displayRemoveConfirmationDialog()
                }
                R.id.item_timer_rename -> {
                    presenter.onClickRenameTimer()
                }
                R.id.item_timer_start_pomodoro -> {
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
            btnPlayPause.setImageResource(R.drawable.ic_pause)
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun displayRemoveConfirmationDialog() {
        val message = resources.getString(R.string.item_timer_remove_message)
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                presenter.onClickDeleteTimer()
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                // do nothing
            }
            .show()
    }

    private fun setupGraph() {
        val component = DaggerTimerItemComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .timerItemModule(TimerItemModule(screen))
            .build()
        component.inject(this)
    }

    private fun createScreen() = object : TimerItemContract.Screen {

        override fun timerUpdated(newTime: Long) {
            chrono.text = TimeConverter.convertSecondsToHumanTime(newTime)
        }

        override fun nameUpdated(newName: String) {
            tvName.text = newName
        }

        override fun displayRenameDialog(oldName: String) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage(resources.getString(R.string.timer_row_rename))

            val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_rename_timer, null)
            val input = rootView.findViewById<EditText>(R.id.edittext)

            input.setText(oldName)
            input.setSelection(input.text.length)

            alertDialog.setView(rootView)

            alertDialog.setPositiveButton(android.R.string.yes) { dialog, which ->
                val newName = input.text.toString()
                presenter.onTimerNameUpdated(newName)
            }

            alertDialog.setNegativeButton(android.R.string.no, { dialog, which -> dialog.cancel() })

            val dialog = alertDialog.create()
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.show()
        }

        override fun statusUpdated(activate: Boolean) {
            updateBtnPlayPause(activate)
        }

        override fun timerRenamed(name: String) {
            tvName.text = name
        }

        override fun startShowMode(id: Long) {
            val action = TimerFragmentDirections.actionTimerListScreenToShowModeScreen(id)
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
    }
}
