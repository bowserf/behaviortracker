package fr.bowser.behaviortracker.create_timer_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = CreateTimerViewComponent::class)
@Component(
    modules = [(CreateTimerViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface CreateTimerViewComponent {

    fun inject(dialog: CreateTimerViewDialog)

    fun provideCreateTimerPresenter(): CreateTimerViewContract.Presenter
}
