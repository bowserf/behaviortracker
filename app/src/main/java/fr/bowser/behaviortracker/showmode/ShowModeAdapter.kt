package fr.bowser.behaviortracker.showmode

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import fr.bowser.behaviortracker.showmodeitem.ShowModeTimerView
import fr.bowser.behaviortracker.timer.Timer

class ShowModeAdapter : PagerAdapter() {

    private lateinit var timers: List<Timer>

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val showModTimerView = ShowModeTimerView(container.context)
        showModTimerView.setTimer(timers[position])
        container.addView(showModTimerView)
        return showModTimerView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `view`: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return timers.size
    }

    fun setData(timers: List<Timer>) {
        this.timers = timers
        notifyDataSetChanged()
    }
}