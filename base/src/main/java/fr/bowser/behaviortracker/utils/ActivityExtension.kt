package fr.bowser.behaviortracker.utils

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

object ActivityExtension {

    private fun <T> lazyNotThreadSafe(initializer: () -> T): Lazy<T> {
        return lazy(LazyThreadSafetyMode.NONE) { initializer() }
    }

    fun <T : View> Activity.bind(@IdRes res: Int): Lazy<T> {
        return lazyNotThreadSafe {
            this.findViewById(res)
        }
    }
}
