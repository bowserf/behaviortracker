package fr.bowser.behaviortracker.scroll_to_timer_manager

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ScrollToTimerManagerModule {

    @Singleton
    @Provides
    fun provideScrollToTimerManager(): ScrollToTimerManager {
        return ScrollToTimerManagerImpl()
    }
}
