package fr.bowser.behaviortracker.timerlist

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = TimerSectionComponent::class)
@Component(
    modules = [(TimerSectionModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface TimerSectionComponent {

    fun inject(view: TimerSectionView)

    fun provideTimerSectionPresenter(): TimerSectionContract.Presenter
}