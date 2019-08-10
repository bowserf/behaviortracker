package fr.bowser.behaviortracker.home

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class HomeManagerModule {

    @Singleton
    @Provides
    fun provideHomeManager(): HomeManager {
        return HomeManagerImpl()
    }
}