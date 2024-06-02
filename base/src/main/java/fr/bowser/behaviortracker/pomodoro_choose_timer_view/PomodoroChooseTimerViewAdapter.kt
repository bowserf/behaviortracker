package fr.bowser.behaviortracker.pomodoro_choose_timer_view

import android.view.LayoutInflater
import android.view.View
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
        val text = view.findViewById<TextView>(R.id.choose_pomodoro_timer_view_item_name)
        return ChooseTimerHolder(view, text)
    }

    override fun getItemCount(): Int {
        return timerList.size
    }

    override fun onBindViewHolder(holder: ChooseTimerHolder, position: Int) {
        holder.text.text = timerList[position].name
        holder.view.setOnClickListener {
            listener.onTimerChose(timerList[position])
        }
    }

    inner class ChooseTimerHolder(
        val view: View,
        val text: TextView,
    ) : RecyclerView.ViewHolder(view)

    interface Listener {
        fun onTimerChose(timer: Timer)
    }
}
