package fr.bowser.behaviortracker.rewards_row_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.billing.InAppManager

@Module
class RewardsRowModule(private val screen: RewardsRowContract.Screen) {

    @GenericScope(component = RewardsRowComponent::class)
    @Provides
    fun provideRewardsRowPresenter(
        inAppManager: InAppManager,
        eventManager: EventManager
    ): RewardsRowContract.Presenter {
        return RewardsRowPresenter(screen, inAppManager, eventManager)
    }
}
