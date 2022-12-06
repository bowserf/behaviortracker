package fr.bowser.behaviortracker.timer_list_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = TimerListViewComponent::class)
@Component(
    modules = [(TimerListViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface TimerListViewComponent {

    fun inject(fragment: TimerListFragment)

    fun provideTimerPresenter(): TimerListViewContract.Presenter
}
