package fr.bowser.feature.toast

import android.content.Context
import fr.bowser.feature.toast.internal.ToastManagerImpl

class ToastModule {

    fun createToastManager(context: Context): ToastManager {
        return ToastManagerImpl(context)
    }
}
