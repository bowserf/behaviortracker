package fr.bowser.behaviortracker.timerlist

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class TimerListGesture(private val callback: GestureCallback) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

    private var isMoving = false

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        isMoving = true
        callback.onItemMove(viewHolder!!.adapterPosition, target!!.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (isMoving && actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            callback.onSelectedChangedUp()
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        isMoving = false
        callback.onSwiped(viewHolder!!.adapterPosition)
    }

    interface GestureCallback {
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onSelectedChangedUp()
        fun onSwiped(position: Int)
    }

}