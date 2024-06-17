package fr.bowser.behaviortracker.clipboard

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.feature_clipboard.CopyDataToClipboardManager
import fr.bowser.feature_string.StringManager
import javax.inject.Singleton

@Module
class CopyDataToClipboardModule {

    @Singleton
    @Provides
    fun provideCopyDataToClipboardManager(
        context: Context,
        stringManager: StringManager,
    ): CopyDataToClipboardManager {
        return fr.bowser.feature_clipboard.CopyDataToClipboardModule(
            context,
            stringManager,
        ).createCopyDataManager()
    }
}
