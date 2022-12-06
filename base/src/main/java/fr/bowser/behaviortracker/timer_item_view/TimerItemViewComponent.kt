package fr.bowser.behaviortracker.timer_item_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = TimerItemViewComponent::class)
@Component(
    modules = [(TimerItemViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface TimerItemViewComponent {

    fun inject(view: TimerItemView)

    fun provideTimerItemPresenter(): TimerItemViewContract.Presenter
}
