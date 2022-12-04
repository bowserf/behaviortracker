package fr.bowser.behaviortracker.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

object FragmentExtension {

    private fun <T> lazyNotThreadSafe(initializer: () -> T): Lazy<T> {
        return lazy(LazyThreadSafetyMode.NONE) { initializer() }
    }

    fun <T : View> Fragment.bind(@IdRes res: Int): Lazy<T> {
        return lazyNotThreadSafe {
            this.requireView().findViewById(res)
        }
    }
}
