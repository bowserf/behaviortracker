package fr.bowser.behaviortracker.timerlist

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class SwipeToDeleteCallback(private val callback: ItemTouchCallback) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        callback.onSwiped(viewHolder!!.adapterPosition)
    }

    interface ItemTouchCallback {
        fun onSwiped(position: Int)
    }

}