package fr.bowser.behaviortracker.string

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.feature_string.StringManager
import fr.bowser.feature_string.StringModule
import javax.inject.Singleton

@Module
class StringManagerModule {

    @Singleton
    @Provides
    fun provideStringManager(
        context: Context
    ): StringManager {
        return StringModule(
            context
        ).createStringManger()
    }
}