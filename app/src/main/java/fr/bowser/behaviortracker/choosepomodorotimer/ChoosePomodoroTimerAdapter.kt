package fr.bowser.behaviortracker.choosepomodorotimer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.timer.Timer

class ChoosePomodoroTimerAdapter(private val context: Context,
                                 private val timerList: List<Timer>,
                                 private val callback: Callback)
    : RecyclerView.Adapter<ChoosePomodoroTimerAdapter.ChooseTimerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseTimerHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.item_pomodoro_timer, parent, false)
        return ChooseTimerHolder(view as TextView)
    }

    override fun getItemCount(): Int {
        return timerList.size
    }

    override fun onBindViewHolder(holder: ChooseTimerHolder, position: Int) {
        holder.view.text = timerList[position].name
        holder.view.setOnClickListener {
            callback.onTimerChose(timerList[position])
        }
    }

    inner class ChooseTimerHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    interface Callback {
        fun onTimerChose(timer: Timer)
    }

}