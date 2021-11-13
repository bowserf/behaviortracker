package fr.bowser.behaviortracker.setting

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.feature_string.StringManager
import javax.inject.Singleton

@Module
class SettingManagerModule {

    @Singleton
    @Provides
    fun provideSettingManager(
        context: Context,
        stringManager: StringManager
    ): SettingManager {
        return SettingManagerImpl(
            context,
            stringManager
        )
    }
}
