package fr.bowser.behaviortracker.pomodoro_choose_timer_view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.timer.Timer

class PomodoroChooseTimerViewAdapter(
    private val timerList: List<Timer>,
    private val listener: Listener
) : RecyclerView.Adapter<PomodoroChooseTimerViewAdapter.ChooseTimerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseTimerHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.choose_pomodoro_timer_view_item,
            parent,
            false
        )
        return ChooseTimerHolder(view as TextView)
    }

    override fun getItemCount(): Int {
        return timerList.size
    }

    override fun onBindViewHolder(holder: ChooseTimerHolder, position: Int) {
        holder.view.text = timerList[position].name
        holder.view.setOnClickListener {
            listener.onTimerChose(timerList[position])
        }
    }

    inner class ChooseTimerHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    interface Listener {
        fun onTimerChose(timer: Timer)
    }
}
