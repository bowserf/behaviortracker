package fr.bowser.behaviortracker.timer_list_view

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.timer_list_section_view.TimerSectionView

class TimerListSectionAdapterDelegate : ListDelegationAdapter<List<TimerListSection>>() {

    init {
        delegatesManager
            .addDelegate(createTimerListSectionAdapterDelegate())
    }

    fun populate(items: List<TimerListSection>) {
        setItems(items)
        notifyDataSetChanged()
    }

    private fun createTimerListSectionAdapterDelegate() =
        adapterDelegate<TimerListSection, TimerListSection>(
            R.layout.timer_list_section_adapter_delegate
        ) {
            val timerSectionView = itemView as TimerSectionView
            bind {
                timerSectionView.populate(item)
            }
        }
}
