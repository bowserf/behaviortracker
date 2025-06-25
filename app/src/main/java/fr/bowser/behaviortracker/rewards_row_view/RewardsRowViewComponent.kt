package fr.bowser.behaviortracker.rewards_row_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = RewardsRowViewComponent::class)
@Component(
    modules = [(RewardsRowViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)],
)
interface RewardsRowViewComponent {

    fun inject(view: RewardsRowView)

    fun provideRewardsRowPresenter(): RewardsRowViewContract.Presenter
}
