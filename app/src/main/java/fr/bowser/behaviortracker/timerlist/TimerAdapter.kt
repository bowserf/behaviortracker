package fr.bowser.behaviortracker.timerlist

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerState
import fr.bowser.behaviortracker.timeritem.TimerRowView

class TimerAdapter : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

    private var timers = ArrayList<TimerState>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TimerViewHolder {
        val timerRowView = TimerRowView(parent!!.context)

        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        timerRowView.layoutParams = layoutParams

        return TimerViewHolder(timerRowView)
    }

    override fun onBindViewHolder(holder: TimerViewHolder?, position: Int) {
        val timer = timers[position]
        holder?.view?.setTimer(timer)
    }

    override fun getItemCount(): Int {
        return timers.size
    }

    fun setTimersList(timers: ArrayList<TimerState>) {
        this.timers = timers
        notifyDataSetChanged()
    }

    fun removeTimer(timer:TimerState){
        timers.remove(timer)
    }

    fun getTimerStateFromTimer(timer:Timer):TimerState{
        timers.filter { it.timer == timer }.forEach { return it }
        throw IllegalStateException("Timer is not in the list of timers")
    }

    inner class TimerViewHolder(val view: TimerRowView) : RecyclerView.ViewHolder(view)

}