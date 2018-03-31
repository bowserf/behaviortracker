package fr.bowser.behaviortracker.timerlist

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timeritem.TimerRowView

class TimerAdapter : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

    private var timerList = ArrayList<Timer>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TimerViewHolder {
        val timerRowView = TimerRowView(parent!!.context)

        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        timerRowView.layoutParams = layoutParams

        return TimerViewHolder(timerRowView)
    }

    override fun onBindViewHolder(holder: TimerViewHolder?, position: Int) {
        val timer = timerList[position]
        holder?.view?.setTimer(timer)
    }

    override fun getItemCount(): Int {
        return timerList.size
    }

    fun setTimersList(timers: List<Timer>) {
        timerList = ArrayList(timers)
        notifyDataSetChanged()
    }

    fun addTimer(timer: Timer) {
        timerList.add(timer)
        val position = timerList.indexOf(timer)
        notifyItemInserted(position)
    }

    fun removeTimer(timer: Timer) {
        val position = timerList.indexOf(timer)
        timerList.remove(timer)
        notifyItemRemoved(position)
    }

    inner class TimerViewHolder(val view: TimerRowView) : RecyclerView.ViewHolder(view)

}