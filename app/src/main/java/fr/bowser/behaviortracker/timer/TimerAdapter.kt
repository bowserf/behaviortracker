package fr.bowser.behaviortracker.timer

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import fr.bowser.behaviortracker.model.Timer

class TimerAdapter(val listener: TimerActionListener) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

    var timers: List<Timer> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TimerViewHolder {
        val timerRowView = TimerRowView(parent!!.context)

        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        timerRowView.setLayoutParams(layoutParams)


        return TimerViewHolder(timerRowView, listener)
    }

    override fun onBindViewHolder(holder: TimerViewHolder?, position: Int) {
        val timer = timers[position]
        holder?.view?.setTimer(timer)
    }

    override fun getItemCount(): Int {
        return timers.size
    }

    fun setTimersList(timers: List<Timer>){
        this.timers = timers
        notifyDataSetChanged()
    }

    inner class TimerViewHolder(val view: TimerRowView, listener: TimerActionListener) : RecyclerView.ViewHolder(view) {

        init{
            view.listener = object: TimerRowView.ActionListener{
                override fun onTimerStateChange() {
                    listener.onTimerStateChange(timers[adapterPosition])
                }

                override fun onClickIncreaseTime() {
                    listener.onClickIncreaseTime(timers[adapterPosition])
                }

                override fun onClickDecreaseTime() {
                    listener.onClickDecreaseTime (timers[adapterPosition])
                }
            }
        }

    }

}