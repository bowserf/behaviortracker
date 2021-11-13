package fr.bowser.behaviortracker.create_timer_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = CreateTimerComponent::class)
@Component(
    modules = [(CreateTimerModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface CreateTimerComponent {

    fun inject(dialog: CreateTimerDialog)

    fun provideCreateTimerPresenter(): CreateTimerContract.Presenter
}
