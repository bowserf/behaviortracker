package fr.bowser.behaviortracker.createtimer

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import fr.bowser.behaviortracker.R

class ColorAdapter(context: Context?, resource: Int, objects: MutableList<Int>?) : ArrayAdapter<Int>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false)
        } else {
            view = convertView
        }
        val color = getItem(position)
        val background = view.findViewById<View>(R.id.item_color).background as GradientDrawable
        background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false)
        } else {
            view = convertView
        }
        val color = getItem(position)
        val background = view.findViewById<View>(R.id.item_color).background as GradientDrawable
        background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        return view
    }

}