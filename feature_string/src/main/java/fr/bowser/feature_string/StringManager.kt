package fr.bowser.feature_string

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface StringManager {

    fun getString(@StringRes stringRes: Int): String

    fun getString(@StringRes stringRes: Int, value: String): String

    fun getStringArray(@ArrayRes arrayRes: Int): Array<String>
}
