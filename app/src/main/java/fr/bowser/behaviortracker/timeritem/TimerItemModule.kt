package fr.bowser.behaviortracker.timeritem

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerItemModule(private val view: TimerItemContract.View) {

    @GenericScope(component = TimerItemComponent::class)
    @Provides
    fun provideTimerItemPresenter(timerManager: TimerManager): TimerItemPresenter{
        return TimerItemPresenter(view, timerManager)
    }

}