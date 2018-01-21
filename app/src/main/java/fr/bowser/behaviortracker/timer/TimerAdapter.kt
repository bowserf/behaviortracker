package fr.bowser.behaviortracker.timer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.model.Timer

class TimerAdapter : RecyclerView.Adapter<TimerViewHolder>() {

    var timers: List<Timer> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TimerViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_timer, parent, false)
        return TimerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerViewHolder?, position: Int) {
        val timer = timers[position]
        holder?.setTimer(timer)
    }

    override fun getItemCount(): Int {
        return timers.size
    }

    fun setTimersList(timers: List<Timer>){
        this.timers = timers
        notifyDataSetChanged()
    }

}