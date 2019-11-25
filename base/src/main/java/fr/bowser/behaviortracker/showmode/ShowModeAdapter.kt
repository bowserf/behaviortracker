package fr.bowser.behaviortracker.showmode

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.showmodeitem.ShowModeTimerView
import fr.bowser.behaviortracker.timer.Timer

class ShowModeAdapter : RecyclerView.Adapter<ShowModeAdapter.ShowModeTimerHolder>() {

    private val timers: ArrayList<Timer> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowModeTimerHolder {
        val showModTimerView = ShowModeTimerView(parent.context)

        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        showModTimerView.layoutParams = layoutParams

        return ShowModeTimerHolder(showModTimerView)
    }

    override fun getItemCount(): Int {
        return timers.size
    }

    override fun onBindViewHolder(holder: ShowModeTimerHolder, position: Int) {
        val timer = timers[position]
        holder.view.setTimer(timer)
    }

    fun setData(timers: List<Timer>) {
        this.timers.clear()
        this.timers.addAll(timers)
        notifyDataSetChanged()
    }

    inner class ShowModeTimerHolder(val view: ShowModeTimerView) : RecyclerView.ViewHolder(view)
}