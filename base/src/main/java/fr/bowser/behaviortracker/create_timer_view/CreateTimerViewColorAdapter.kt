package fr.bowser.behaviortracker.create_timer_view

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.utils.ColorUtils

class CreateTimerViewColorAdapter(
    private val context: Context,
    initialSelectedColorPosition: Int,
    private val callback: Callback,
) : RecyclerView.Adapter<CreateTimerViewColorAdapter.ColorViewHolder>() {

    private var selectedItemPosition = initialSelectedColorPosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.create_timer_view_color,
            parent,
            false,
        )
        return ColorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ColorUtils.NUMBER_COLORS
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = ColorUtils.getColor(context, position)
        val background = holder.colorView.background as GradientDrawable
        background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        if (position == selectedItemPosition) {
            holder.colorView.setImageResource(R.drawable.create_timer_view_check)
        } else {
            holder.colorView.setImageResource(0)
        }

        holder.colorView.drawable

        holder.view.setOnClickListener {
            callback.onChangeSelectedColor(selectedItemPosition, position)
            selectedItemPosition = position
        }
    }

    inner class ColorViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val colorView: ImageView = view.findViewById(R.id.item_color)
    }

    interface Callback {
        fun onChangeSelectedColor(oldSelectedPosition: Int, selectedPosition: Int)
    }
}
