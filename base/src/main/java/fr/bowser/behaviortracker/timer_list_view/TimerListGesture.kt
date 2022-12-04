package fr.bowser.behaviortracker.timer_list_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.R

class TimerListGesture(context: Context, private val callback: Listener) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT
    ) {

    private var isMoving = false

    private val icon: Bitmap
    private val rightMargin: Int

    init {
        icon = getBitmapFromVectorDrawable(context, R.drawable.timer_list_view_delete)
        rightMargin =
            context.resources.getDimensionPixelOffset(R.dimen.timer_list_view_remove_icon_margin_right)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        isMoving = true
        callback.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (isMoving && actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            callback.onSelectedChangedUp()
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        isMoving = false
        callback.onSwiped(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            if (dX < 0) {
                val progress = Math.min(Math.abs(dX) / viewHolder.itemView.width.toFloat(), 1f)
                val left = itemView.right.toFloat() - rightMargin - icon.width
                val top = itemView.top.toFloat() + (itemView.height - icon.height) / 2
                c.rotate(360 * progress, left + icon.width / 2, top + icon.height / 2)
                c.drawBitmap(icon, left, top, null)
            }

            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val ratio = 1.5f
        val width = (drawable!!.intrinsicWidth * ratio).toInt()
        val height = (drawable.intrinsicHeight * ratio).toInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    interface Listener {
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onSelectedChangedUp()
        fun onSwiped(position: Int)
    }
}
