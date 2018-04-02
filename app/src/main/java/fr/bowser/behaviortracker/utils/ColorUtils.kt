package fr.bowser.behaviortracker.utils


import android.content.Context
import android.support.v4.content.ContextCompat
import fr.bowser.behaviortracker.R


object ColorUtils {

    const val NUMBER_COLORS = 19

    private const val COLOR_DEEP_ORANGE = 0
    private const val COLOR_BROWN = 1
    private const val COLOR_GREY = 2
    private const val COLOR_BLUE_GREY = 3
    private const val COLOR_YELLOW = 4
    private const val COLOR_AMBER = 5
    private const val COLOR_ORANGE = 6
    private const val COLOR_GREEN = 7
    private const val COLOR_LIGHT_GREEN = 8
    private const val COLOR_LIME = 9
    private const val COLOR_LIGHT_BLUE = 10
    private const val COLOR_CYAN = 11
    private const val COLOR_TEAL = 12
    private const val COLOR_DEEP_PURPLE = 13
    private const val COLOR_INDIGO = 14
    private const val COLOR_BLUE = 15
    private const val COLOR_RED = 16
    private const val COLOR_PINK = 17
    private const val COLOR_PURPLE = 18

    fun getColor(context: Context, colorValue: Int): Int {
        val colorRes: Int
        when (colorValue) {
            COLOR_DEEP_ORANGE -> colorRes = R.color.deep_orange
            COLOR_BROWN -> colorRes = R.color.brown
            COLOR_GREY -> colorRes = R.color.grey
            COLOR_BLUE_GREY -> colorRes = R.color.blue_grey
            COLOR_YELLOW -> colorRes = R.color.yellow
            COLOR_AMBER -> colorRes = R.color.amber
            COLOR_ORANGE -> colorRes = R.color.orange
            COLOR_GREEN -> colorRes = R.color.gren
            COLOR_LIGHT_GREEN -> colorRes = R.color.light_green
            COLOR_LIME -> colorRes = R.color.lime
            COLOR_LIGHT_BLUE -> colorRes = R.color.light_blue
            COLOR_CYAN -> colorRes = R.color.cyan
            COLOR_TEAL -> colorRes = R.color.teal
            COLOR_DEEP_PURPLE -> colorRes = R.color.deep_purple
            COLOR_INDIGO -> colorRes = R.color.indigo
            COLOR_BLUE -> colorRes = R.color.blue
            COLOR_RED -> colorRes = R.color.red
            COLOR_PINK -> colorRes = R.color.pink
            COLOR_PURPLE -> colorRes = R.color.purple
            else -> throw IllegalStateException("This color value doesn't exist : $colorValue")
        }
        return ContextCompat.getColor(context, colorRes)
    }
}
