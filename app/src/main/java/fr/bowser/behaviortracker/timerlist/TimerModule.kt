package fr.bowser.behaviortracker.home

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timerlist.TimerPresenter
import javax.inject.Singleton

@Module
internal class TimerModule(private val timerView: TimerContract.View, private val timerManager: TimerManager) {

    @Singleton
    @Provides
    fun provideTimerPresenter(): TimerPresenter {
        return TimerPresenter(timerView, timerManager)
    }

}
