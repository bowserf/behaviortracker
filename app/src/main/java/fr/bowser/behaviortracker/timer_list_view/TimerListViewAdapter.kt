package fr.bowser.behaviortracker.timer_list_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_item_view.TimerItemView

class TimerListViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val timerList = mutableListOf<Timer>()
    private var totalTime = 0L

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return if (position < timerList.size) {
            timerList[position].id
        } else {
            -1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < timerList.size) {
            VIEW_TYPE_TIMER
        } else {
            VIEW_TYPE_TOTAL_TIME
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_TIMER) {
            val timerRowView = TimerItemView(parent.context)

            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            val horizontalMargin =
                parent.resources.getDimensionPixelOffset(R.dimen.default_space_1_5)
            layoutParams.setMargins(horizontalMargin, 0, horizontalMargin, 0)
            timerRowView.layoutParams = layoutParams

            return TimerViewHolder(timerRowView)
        } else {
            val totalTimeView = TotalTimeView(parent.context)
            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            totalTimeView.layoutParams = layoutParams
            return TotalTimeViewHolder(totalTimeView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TimerViewHolder) {
            val timer = timerList[position]
            holder.view.setTimer(timer)
        } else if (holder is TotalTimeViewHolder) {
            holder.view.setTotalTime(totalTime)
        }
    }

    override fun getItemCount(): Int {
        return if (timerList.isNotEmpty()) {
            timerList.size + 1
        } else {
            0
        }
    }

    fun populate(timers: List<Timer>, update: Boolean) {
        timerList.clear()
        timerList.addAll(timers)
        if (update) {
            notifyDataSetChanged()
        }
    }

    fun updateTotalTime(totalTime: Long) {
        this.totalTime = totalTime
        notifyItemChanged(timerList.size)
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

    class TimerViewHolder(val view: TimerItemView) : RecyclerView.ViewHolder(view)

    class TotalTimeViewHolder(val view: TotalTimeView) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_TIMER = 0
        private const val VIEW_TYPE_TOTAL_TIME = 1
    }
}
