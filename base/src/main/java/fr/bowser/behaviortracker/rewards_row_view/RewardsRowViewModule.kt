package fr.bowser.behaviortracker.rewards_row_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.billing.InAppManager

@Module
class RewardsRowViewModule(private val screen: RewardsRowViewContract.Screen) {

    @GenericScope(component = RewardsRowViewComponent::class)
    @Provides
    fun provideRewardsRowPresenter(
        inAppManager: InAppManager,
        eventManager: EventManager
    ): RewardsRowViewContract.Presenter {
        return RewardsRowViewPresenter(screen, inAppManager, eventManager)
    }
}
