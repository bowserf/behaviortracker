package fr.bowser.behaviortracker.rewards_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.inapp.InAppConfiguration
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.billing.InAppManager

@Module
class RewardsViewModule(private val screen: RewardsViewContract.Screen) {

    @GenericScope(component = RewardsViewComponent::class)
    @Provides
    fun provideRewardsPresenter(
        inAppConfiguration: InAppConfiguration,
        inAppManager: InAppManager
    ): RewardsViewContract.Presenter {
        return RewardsViewPresenter(screen, inAppConfiguration, inAppManager)
    }
}
