package fr.bowser.behaviortracker.utils

import android.view.View
import androidx.annotation.IdRes

object ViewExtension {

    private fun <T> lazyNotThreadSafe(initializer: () -> T): Lazy<T> {
        return lazy(LazyThreadSafetyMode.NONE) { initializer() }
    }

    fun <T : View> View.bind(@IdRes res: Int): Lazy<T> {
        return lazyNotThreadSafe {
            this.findViewById(res)
        }
    }
}
