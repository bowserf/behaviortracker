package fr.bowser.behaviortracker.timer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerManagerModule {

    @GenericScope(component = BehaviorTrackerAppComponent::class)
    @Provides
    fun provideTimerManager(): TimerManager {
        return TimerManager()
    }

}