package fr.bowser.behaviortracker.toast

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.feature.toast.ToastManager
import fr.bowser.feature.toast.ToastModule
import javax.inject.Singleton

@Module
class ToastManagerModule {

    @Singleton
    @Provides
    fun provideToastManager(
        context: Context,
    ): ToastManager {
        return ToastModule().createToastManager(context)
    }
}
