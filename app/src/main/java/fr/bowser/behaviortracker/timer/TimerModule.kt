package fr.bowser.behaviortracker.home

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerPresenter
import javax.inject.Singleton

@Module
internal class TimerModule(private val timerView: TimerContract.View) {

    @Singleton
    @Provides
    fun provideTimerPresenter(): TimerPresenter {
        return TimerPresenter(timerView)
    }

}
