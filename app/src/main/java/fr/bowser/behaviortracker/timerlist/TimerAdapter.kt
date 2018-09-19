package fr.bowser.behaviortracker.timerlist

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timeritem.TimerRowView
import java.util.*

class TimerAdapter : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

    private val timerList = mutableListOf<Timer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val timerRowView = TimerRowView(parent.context)

        val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

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

    fun setTimersList(timers: List<Timer>) {
        timerList.clear()
        timerList.addAll(timers)
        notifyDataSetChanged()
    }

    fun addTimer(timer: Timer) {
        timerList.add(timer.position, timer)
        val position = timerList.indexOf(timer)
        notifyItemInserted(position)
    }

    fun removeTimer(timer: Timer) {
        val position = timerList.indexOf(timer)
        if (position != -1) {
            timerList.remove(timer)
            notifyItemRemoved(position)
        }
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(timerList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(timerList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getTimer(position: Int): Timer {
        return timerList[position]
    }

    fun getTimerList(): List<Timer> {
        return timerList.toList()
    }

    inner class TimerViewHolder(val view: TimerRowView) : RecyclerView.ViewHolder(view)

}