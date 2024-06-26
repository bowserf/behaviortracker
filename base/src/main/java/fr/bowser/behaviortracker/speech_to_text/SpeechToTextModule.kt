package fr.bowser.behaviortracker.speech_to_text

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.speech_to_text.internal.SpeechToTextManagerImpl
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.time_provider.TimeProviderImpl
import javax.inject.Singleton

@Module
class SpeechToTextModule {

    @Singleton
    @Provides
    fun provideSpeechToTextManager(context: Context): SpeechToTextManager {
        return SpeechToTextManagerImpl(context)
    }
}
