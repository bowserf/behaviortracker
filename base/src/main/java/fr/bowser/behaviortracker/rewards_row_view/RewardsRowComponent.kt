package fr.bowser.behaviortracker.rewards_row_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = RewardsRowComponent::class)
@Component(
    modules = [(RewardsRowModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface RewardsRowComponent {

    fun inject(view: RewardsRowView)

    fun provideRewardsRowPresenter(): RewardsRowContract.Presenter
}
