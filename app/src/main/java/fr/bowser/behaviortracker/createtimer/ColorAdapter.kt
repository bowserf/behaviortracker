package fr.bowser.behaviortracker.createtimer

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.utils.ColorUtils

class ColorAdapter(val context: Context, val presenter: CreateTimerPresenter) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var selectedItemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ColorUtils.NUMBER_COLORS
    }

    override fun onBindViewHolder(holder: ColorViewHolder?, position: Int) {
        val color = ColorUtils.getColor(context, position)
        val background = holder?.colorView?.background as GradientDrawable?
        background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        if (position == selectedItemPosition) {
            holder?.colorView?.setImageResource(R.drawable.ic_check)
        } else {
            holder?.colorView?.setImageResource(0)
        }

        holder?.colorView?.drawable

        holder?.view?.setOnClickListener {
            presenter.changeSelectedColor(selectedItemPosition, position)
            selectedItemPosition = position
        }
    }

    inner class ColorViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val colorView: ImageView = view.findViewById(R.id.item_color)

    }

}