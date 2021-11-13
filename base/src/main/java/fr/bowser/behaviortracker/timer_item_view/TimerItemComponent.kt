package fr.bowser.behaviortracker.timer_item_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = TimerItemComponent::class)
@Component(
    modules = [(TimerItemModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface TimerItemComponent {

    fun inject(view: TimerRowView)

    fun provideTimerItemPresenter(): TimerItemContract.Presenter
}
