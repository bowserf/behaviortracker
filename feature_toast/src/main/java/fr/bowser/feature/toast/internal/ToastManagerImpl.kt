package fr.bowser.feature.toast.internal

import android.content.Context
import android.widget.Toast
import fr.bowser.feature.toast.ToastManager

internal class ToastManagerImpl(
    private val context: Context,
) : ToastManager {

    override fun showText(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT,
        ).show()
    }

    override fun showText(textRes: Int) {
        showText(context.resources.getString(textRes))
    }
}
