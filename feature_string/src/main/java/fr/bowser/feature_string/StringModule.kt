package fr.bowser.feature_string

import android.content.Context
import fr.bowser.feature_string.internal.StringManagerImpl

class StringModule(
    private val context: Context
) {

    fun createStringManger(): StringManager {
        return StringManagerImpl(context)
    }
}
