package fr.bowser.behaviortracker.clipboard

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.feature_clipboard.CopyDataToClipboardManager
import fr.bowser.feature_do_not_disturb.DoNotDisturbGraph
import fr.bowser.feature_string.StringManager
import javax.inject.Singleton

@Module
class CopyDataToClipboardModule {

    @Singleton
    @Provides
    fun provideCopyDataToClipboardManager(
        context: Context,
        stringManager: StringManager
    ): CopyDataToClipboardManager {
        return fr.bowser.feature_clipboard.CopyDataToClipboardModule(
            context,
            stringManager
        ).createCopyDataManager()
    }
}
