package fr.bowser.behaviortracker.pomodoro

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Spinner


class SpinnerReselect(context: Context, attrs: AttributeSet? = null) : Spinner(context, attrs) {

    override fun setSelection(position: Int) {
        ignoreOldSelectionByReflection()
        super.setSelection(position)
    }

    override fun setSelection(position: Int, animate: Boolean) {
        ignoreOldSelectionByReflection()
        super.setSelection(position, animate)
    }

    private fun ignoreOldSelectionByReflection() {
        try {
            val c = this.javaClass.superclass.superclass.superclass
            val reqField = c.getDeclaredField("mOldSelectedPosition")
            reqField.isAccessible = true
            reqField.setInt(this, -1)
        } catch (e: Exception) {
            Log.d("Exception Private", "ex", e)
        }

    }
}