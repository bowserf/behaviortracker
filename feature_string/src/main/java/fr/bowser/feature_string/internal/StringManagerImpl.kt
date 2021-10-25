package fr.bowser.feature_string.internal

import android.content.Context
import androidx.annotation.StringRes
import fr.bowser.feature_string.StringManager

internal class StringManagerImpl(
    private val context: Context
) : StringManager {

    override fun getString(@StringRes stringRes: Int): String {
        return context.resources.getString(stringRes)
    }

    override fun getStringArray(arrayRes: Int): Array<String> {
        return context.resources.getStringArray(arrayRes)
    }
}
