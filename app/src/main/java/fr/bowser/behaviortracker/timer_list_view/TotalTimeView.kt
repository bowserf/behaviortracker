package fr.bowser.behaviortracker.timer_list_view

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.utils.TimeConverter
import fr.bowser.behaviortracker.utils.ViewExtension.bind

class TotalTimeView(context: Context) : FrameLayout(context) {

    private val totalTimeTv: TextView by bind(R.id.total_time_item_view_text)

    init {
        inflate(context, R.layout.total_time_item_view, this)
    }

    fun setTotalTime(totalTime: Long) {
        val totalTimeStr = TimeConverter.convertSecondsToHumanTime(totalTime)
        totalTimeTv.text = resources.getString(
            R.string.timer_list_total_time,
            totalTimeStr,
        )
    }
}
