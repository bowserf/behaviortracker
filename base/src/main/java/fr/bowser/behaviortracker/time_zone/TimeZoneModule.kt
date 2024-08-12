package fr.bowser.behaviortracker.time_zone

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TimeZoneModule {

    @Singleton
    @Provides
    fun provideTimeZoneManager(): TimeZoneManager {
        return TimeZoneManagerImpl()
    }
}
