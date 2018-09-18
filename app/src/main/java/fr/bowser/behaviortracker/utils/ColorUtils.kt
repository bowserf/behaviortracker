package fr.bowser.behaviortracker.utils


import android.content.Context
import androidx.core.content.ContextCompat
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

    fun convertPositionToColor(colorPosition: Int): Int {
        val color: Int
        when (colorPosition) {
                0 -> color = COLOR_DEEP_ORANGE
                1 -> color = COLOR_BROWN
                2 -> color = COLOR_GREY
                3 -> color = COLOR_BLUE_GREY
                4 -> color = COLOR_YELLOW
                5 -> color = COLOR_AMBER
                6 -> color = COLOR_ORANGE
                7 -> color = COLOR_GREEN
                8 -> color = COLOR_LIGHT_GREEN
                9 -> color = COLOR_LIME
                10 -> color = COLOR_LIGHT_BLUE
                11-> color = COLOR_CYAN
                12 -> color = COLOR_TEAL
                13 -> color = COLOR_DEEP_PURPLE
                14 -> color = COLOR_INDIGO
                15 -> color = COLOR_BLUE
                16 -> color = COLOR_RED
                17 -> color = COLOR_PINK
                18 -> color = COLOR_PURPLE
            else -> throw IllegalStateException("This position is not managed : $colorPosition")
        }
        return color
    }

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
