package fr.bowser.behaviortracker.timer

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.model.Timer
import fr.bowser.behaviortracker.utils.TimeConverter

class TimerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val chrono: TextView = itemView.findViewById(R.id.timer_chrono)
    private val name: TextView = itemView.findViewById(R.id.timer_name)
    private val menu: ImageView = itemView.findViewById(R.id.timer_menu)
    private val reduceChrono:TextView = itemView.findViewById(R.id.timer_reduce_time)
    private val increateChrono: TextView = itemView.findViewById(R.id.timer_increase_time)

    fun setTimer(timer: Timer) {
        itemView.setBackgroundColor(timer.color)

        chrono.text = TimeConverter.convertSecondsToHumanTime(timer.time)
        name.text = timer.name
    }

}