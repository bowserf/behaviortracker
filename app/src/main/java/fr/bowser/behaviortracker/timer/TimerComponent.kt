package fr.bowser.behaviortracker.home


import dagger.Component
import fr.bowser.behaviortracker.timer.TimerFragment
import fr.bowser.behaviortracker.timer.TimerPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TimerModule::class))
interface TimerComponent {

    fun inject(fragment: TimerFragment)

    fun provideTimerPresenter(): TimerPresenter

}
