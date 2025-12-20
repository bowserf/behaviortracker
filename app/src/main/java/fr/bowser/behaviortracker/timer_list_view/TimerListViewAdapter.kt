package fr.bowser.behaviortracker.timer_list_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_item_view.TimerItemView

class TimerListViewAdapter : RecyclerView.Adapter<TimerListViewAdapter.TimerViewHolder>() {

    private val timerList = mutableListOf<Timer>()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return timerList[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val timerRowView = TimerItemView(parent.context)

        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        val horizontalMargin = parent.resources.getDimensionPixelOffset(R.dimen.default_space_1_5)
        layoutParams.setMargins(horizontalMargin, 0, horizontalMargin, 0)
        timerRowView.layoutParams = layoutParams

        return TimerViewHolder(timerRowView)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        val timer = timerList[position]
        holder.view.setTimer(timer)
    }

    override fun getItemCount(): Int {
        return timerList.size
    }

    fun populate(timers: List<Timer>, update: Boolean) {
        timerList.clear()
        timerList.addAll(timers)
        if (update) {
            notifyDataSetChanged()
        }
    }

    fun addTimer(timer: Timer) {
        timerList.add(timer.position, timer)
        val position = timerList.indexOf(timer)
        notifyItemInserted(position)
    }

    fun getTimerList(): List<Timer> {
        return timerList.toList()
    }

    fun removeTimer(position: Int) {
        notifyItemRemoved(position)
    }

    fun reorderTimer(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class TimerViewHolder(val view: TimerItemView) : RecyclerView.ViewHolder(view)
}
