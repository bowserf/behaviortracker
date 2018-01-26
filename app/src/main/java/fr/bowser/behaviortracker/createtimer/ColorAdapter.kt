package fr.bowser.behaviortracker.createtimer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import fr.bowser.behaviortracker.R

class ColorAdapter(context: Context?, resource: Int, objects: MutableList<Int>?) : ArrayAdapter<Int>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false)
        }else{
            view = convertView
        }
        val color = getItem(position)
        view.setBackgroundColor(color)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_color_drop_down, parent, false)
        }else{
            view = convertView
        }
        val color = getItem(position)
        view.findViewById<View>(R.id.item_color_drop_down_color).setBackgroundColor(color)
        return view
    }

}