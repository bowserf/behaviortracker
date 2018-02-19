package fr.bowser.behaviortracker.timeritem

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.TimerState
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject


class TimerRowView(context: Context) :
        CardView(context),
        TimerItemContract.View {

    private val chrono: TextView
    private val name: TextView
    private val menu: ImageView
    private val reduceChrono: TextView
    private val increaseChrono: TextView
    private val color: View
    private val btnPlayPause: ImageView

    @Inject
    lateinit var presenter: TimerItemPresenter

    init {
        setupGraph()

        inflate(context, R.layout.item_timer, this)

        // card radius
        radius = resources.getDimension(R.dimen.default_space_half)

        setOnClickListener { manageClickPlayPauseButton() }

        btnPlayPause = findViewById(R.id.timer_play_pause)

        color = findViewById(R.id.timer_color)
        color.setOnClickListener { /*TODO display show mode */}
        chrono = findViewById(R.id.timer_chrono)
        name = findViewById(R.id.timer_name)
        menu = findViewById(R.id.timer_menu)
        menu.setOnClickListener { displayMenu() }
        reduceChrono = findViewById(R.id.timer_reduce_time)
        reduceChrono.setOnClickListener { presenter.onClickDecreaseTime() }
        increaseChrono = findViewById(R.id.timer_increase_time)
        increaseChrono.setOnClickListener { presenter.onClickIncreaseTime() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.start()
    }

    override fun onDetachedFromWindow() {
        presenter.stop()
        super.onDetachedFromWindow()
    }

    fun setTimer(timerState: TimerState) {
        presenter.setTimer(timerState)

        chrono.text = TimeConverter.convertSecondsToHumanTime(timerState.timer.currentTime)
        name.text = timerState.timer.name
        color.setBackgroundColor(ColorUtils.getColor(context!!, timerState.timer.color))

        updateBtnPlayPause(timerState.isActivate)
    }

    override fun timerUpdated(newTime: Long) {
        chrono.text = TimeConverter.convertSecondsToHumanTime(newTime)
    }

    override fun nameUpdated(newName: String) {
        name.text = newName
    }

    private fun displayMenu() {
        val popup = PopupMenu(context, menu)

        popup.menuInflater.inflate(R.menu.menu_item_timer, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_timer_reset -> {
                    presenter.onClickResetTimer()
                }
                R.id.item_timer_delete -> {
                    displayRemoveConfirmationDialog()
                }
                R.id.item_timer_rename -> {
                    presenter.onClickRenameTimer()
                }
            }
            true
        }

        popup.show()
    }

    private fun manageClickPlayPauseButton() {
        val isActivate = presenter.onTimerStateChange()
        updateBtnPlayPause(isActivate)
    }

    private fun updateBtnPlayPause(isActivate:Boolean){
        if(isActivate){
            btnPlayPause.setImageResource(R.drawable.ic_pause)
        }else {
            btnPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    override fun displayRenameDialog(oldName: String) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(resources.getString(R.string.timer_row_rename))

        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_rename_timer, null)
        val input = rootView.findViewById<EditText>(R.id.edittext)

        input.setText(oldName)

        alertDialog.setView(rootView)

        alertDialog.setPositiveButton(android.R.string.yes, { dialog, which ->
            val newName = input.text.toString()
            name.text = newName
            presenter.onTimerNameUpdated(newName)
        })

        alertDialog.setNegativeButton(android.R.string.no, { dialog, which -> dialog.cancel() })

        alertDialog.show()
    }

    override fun statusUpdated(activate: Boolean) {
        updateBtnPlayPause(activate)
    }

    private fun displayRemoveConfirmationDialog() {
        val message = resources.getString(R.string.item_timer_remove_message)
        val builder = AlertDialog.Builder(context)
        builder.setTitle(message)
                .setPositiveButton(android.R.string.yes, { dialog, which ->
                    presenter.onClickDeleteTimer()
                })
                .setNegativeButton(android.R.string.no, { dialog, which ->
                    // do nothing
                })
                .show()
    }

    private fun setupGraph() {
        val component = DaggerTimerItemComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
                .timerItemModule(TimerItemModule(this))
                .build()
        component.inject(this)
    }

}