package fr.bowser.behaviortracker.update_timer_time_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = UpdateTimerTimeComponent::class)
@Component(
    modules = [(UpdateTimerTimeModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface UpdateTimerTimeComponent {

    fun inject(updateTimerTimeDialog: UpdateTimerTimeDialog)

    fun provideUpdateTimerTimePresenter(): UpdateTimerTimeContract.Presenter
}
