package fr.bowser.behaviortracker.createtimer

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import fr.bowser.behaviortracker.R

class ColorAdapter(val context: Context, val presenter: CreateTimerPresenter) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private val colorsList = provideColors()

    private var selectedItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return colorsList.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder?, position: Int) {
        val color = colorsList[position]
        val background = holder?.colorView?.background as GradientDrawable?
        background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        if(position == selectedItemPosition) {
            holder?.colorView?.setImageResource(R.drawable.ic_check)
        }else{
            holder?.colorView?.setImageResource(0)
        }

        holder?.colorView?.drawable

        holder?.view?.setOnClickListener {
            presenter.changeSelectedColor(color, selectedItemPosition, position)

            selectedItemPosition = position
        }
    }

    private fun provideColors(): MutableList<Int> {
        val colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(context, R.color.purple))
        colors.add(ContextCompat.getColor(context, R.color.pink))
        colors.add(ContextCompat.getColor(context, R.color.red))
        colors.add(ContextCompat.getColor(context, R.color.blue))
        colors.add(ContextCompat.getColor(context, R.color.indigo))
        colors.add(ContextCompat.getColor(context, R.color.deep_purple))
        colors.add(ContextCompat.getColor(context, R.color.teal))
        colors.add(ContextCompat.getColor(context, R.color.cyan))
        colors.add(ContextCompat.getColor(context, R.color.light_blue))
        colors.add(ContextCompat.getColor(context, R.color.lime))
        colors.add(ContextCompat.getColor(context, R.color.light_green))
        colors.add(ContextCompat.getColor(context, R.color.gren))
        colors.add(ContextCompat.getColor(context, R.color.amber))
        colors.add(ContextCompat.getColor(context, R.color.orange))
        colors.add(ContextCompat.getColor(context, R.color.deep_orange))
        colors.add(ContextCompat.getColor(context, R.color.brown))
        colors.add(ContextCompat.getColor(context, R.color.grey))
        colors.add(ContextCompat.getColor(context, R.color.blue_grey))
        return colors
    }

    inner class ColorViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val colorView = view.findViewById<ImageView>(R.id.item_color)

    }

    companion object {
        val SCALE_ANIMATION_DURATION = 1200L
    }

}