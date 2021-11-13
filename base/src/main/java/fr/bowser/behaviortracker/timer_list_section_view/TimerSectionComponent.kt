package fr.bowser.behaviortracker.timer_list_section_view

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
