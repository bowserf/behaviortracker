package fr.bowser.behaviortracker.timerlist

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerModule(private val timerView: TimerContract.View) {

    @GenericScope(component = TimerComponent::class)
    @Provides
    fun provideTimerPresenter(): TimerPresenter {
        return TimerPresenter(timerView)
    }

}
