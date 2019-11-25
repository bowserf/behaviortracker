package fr.bowser.behaviortracker.setting

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingManagerModule {

    @Singleton
    @Provides
    fun provideSettingManager(context: Context): SettingManager {
        return SettingManagerImpl(context)
    }
}