package fr.bowser.behaviortracker.time

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TimeProviderModule {

    @Singleton
    @Provides
    fun provideTimeProvider(): TimeProvider {
        return TimeProviderImpl()
    }
}