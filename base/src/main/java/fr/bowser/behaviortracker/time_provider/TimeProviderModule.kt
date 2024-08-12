package fr.bowser.behaviortracker.time_provider

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.time_zone.TimeZoneManager
import javax.inject.Singleton

@Module
class TimeProviderModule {

    @Singleton
    @Provides
    fun provideTimeProvider(
        timeZoneManager: TimeZoneManager,
    ): TimeProvider {
        return TimeProviderImpl(timeZoneManager)
    }
}
