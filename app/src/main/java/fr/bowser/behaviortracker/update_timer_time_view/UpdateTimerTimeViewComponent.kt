package fr.bowser.behaviortracker.update_timer_time_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = UpdateTimerTimeViewComponent::class)
@Component(
    modules = [(UpdateTimerTimeViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)],
)
interface UpdateTimerTimeViewComponent {

    fun inject(updateTimerTimeDialog: UpdateTimerTimeDialog)

    fun provideUpdateTimerTimePresenter(): UpdateTimerTimeViewContract.Presenter
}
