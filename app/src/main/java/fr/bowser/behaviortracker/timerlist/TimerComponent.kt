package fr.bowser.behaviortracker.home


import dagger.Component
import fr.bowser.behaviortracker.timerlist.TimerFragment
import fr.bowser.behaviortracker.timerlist.TimerPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TimerModule::class))
interface TimerComponent {

    fun inject(fragment: TimerFragment)

    fun provideTimerPresenter(): TimerPresenter

}
